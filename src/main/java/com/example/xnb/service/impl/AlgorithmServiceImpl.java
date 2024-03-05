package com.example.xnb.service.impl;

import com.example.xnb.entity.Coin;
import com.example.xnb.entity.CandlestickChart;
import com.example.xnb.service.AlgorithmService;
import com.example.xnb.service.ICandlestickChartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * .
 * 2023/11/21 9:45 下午
 */

@Service
public class AlgorithmServiceImpl implements AlgorithmService {

    @Autowired
    private ICandlestickChartService candlestickChartService;

    @Override
    public void addCoinGenerateData(Coin coin) {
        List<CandlestickChart> candlestickChartList = new ArrayList<>();
        List<LocalDateTime> timeList = this.generateTimeList(LocalDateTime.parse("2024-01-01T00:00:00"));
        Random random = new Random();
        int max = Math.max(coin.getStartPrice().multiply(new BigDecimal(100)).intValue(), coin.getPrice().multiply(new BigDecimal(100)).intValue()) * 120 / 100;
        int min = Math.min(coin.getStartPrice().multiply(new BigDecimal(100)).intValue(), coin.getPrice().multiply(new BigDecimal(100)).intValue()) * 80 / 100;
        for (LocalDateTime time : timeList) {
            CandlestickChart candlestickChart = new CandlestickChart();
            candlestickChart.setTime(time);
            candlestickChart.setCoinId(coin.getId());
            int price = min + random.nextInt(max - min + 1);
            candlestickChart.setPrice(new BigDecimal(price).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP));

            int thisTimeMax = price * 110 / 100;
            int thisTimeMin = price * 90 / 100;

            candlestickChart.setMaxPrice(new BigDecimal(price + random.nextInt(thisTimeMax - price + 1)).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP));
            candlestickChart.setMinPrice(new BigDecimal(thisTimeMin + random.nextInt(price - thisTimeMin + 1)).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP));
            candlestickChartList.add(candlestickChart);
        }
        candlestickChartService.saveBatch(candlestickChartList);
    }

    @Override
    public List<LocalDateTime> generateTimeList(LocalDateTime issueTime) {
        List<LocalDateTime> list = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        now.withSecond(0);
        if (now.getMinute() > 0 && now.getMinute() < 30) {
            now.withMinute(30);
        } else if (now.getMinute() > 30) {
            now.withMinute(0).plusHours(1);
        }

        LocalDateTime current = issueTime;
        while (current.isBefore(now) || current.isEqual(now)) {
            list.add(current);
            // Increment by half an hour
            current = current.plusMinutes(30);
        }
        return list;
    }
}
