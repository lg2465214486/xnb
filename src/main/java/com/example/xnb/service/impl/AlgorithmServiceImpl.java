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

    private static final Integer DAY_OF_INCREASE = 5;
    private static final Integer INCREASE = 20;
    private static final Random MY_RANDOM = new Random();

    @Override
    public void addCoinGenerateData(Coin coin) {
        LocalDateTime now = LocalDateTime.now();
        now = this.convertThirtyMinute(now);
        LocalDateTime startTime = LocalDateTime.now().minusMonths(3);
        startTime = this.convertThirtyMinute(startTime);
        LocalDateTime midTime = LocalDateTime.now().minusMonths(1);
        midTime = this.convertThirtyMinute(midTime);

        int max = Math.max(coin.getStartPrice().multiply(new BigDecimal(100)).intValue(), coin.getPrice().multiply(new BigDecimal(100)).intValue()) * (INCREASE + 100) / 100;
        int min = Math.min(coin.getStartPrice().multiply(new BigDecimal(100)).intValue(), coin.getPrice().multiply(new BigDecimal(100)).intValue()) * (100 - INCREASE) / 100;

        List<CandlestickChart> candlestickChartList = new ArrayList<>();
        List<LocalDateTime> timeList = this.generateTimeList(startTime, midTime);
        List<LocalDateTime> lastTimeList = this.generateTimeList(midTime, now);

        Integer startPriceOf24Hours = coin.getStartPrice().multiply(new BigDecimal(100)).intValue();
        List<Integer> datas = null;
        int count=48;
        for (int i = 0; i < timeList.size(); i++) {
            if (count == 48) {
                int flag = MY_RANDOM.nextInt(3);
                if (startPriceOf24Hours * (DAY_OF_INCREASE + 100) / 100 > max) {
                    flag = 2;
                } else if (startPriceOf24Hours * (100 - DAY_OF_INCREASE) / 100 < min) {
                    flag = 1;
                }
                datas = this.generate24HoursData(startPriceOf24Hours, flag);
                startPriceOf24Hours = datas.get(47);
                count=0;
            }
            CandlestickChart candlestickChart = new CandlestickChart();
            candlestickChart.setTime(timeList.get(i));
            candlestickChart.setCoinId(coin.getId());
            int price = datas.get(i % 48);
            candlestickChart.setPrice(new BigDecimal(price).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP));

            int thisTimeMax = price * 110 / 100;
            int thisTimeMin = price * 90 / 100;

            candlestickChart.setMaxPrice(new BigDecimal(price + MY_RANDOM.nextInt(thisTimeMax - price + 1)).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP));
            candlestickChart.setMinPrice(new BigDecimal(thisTimeMin + MY_RANDOM.nextInt(price - thisTimeMin + 1)).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP));
            candlestickChartList.add(candlestickChart);
            count++;
        }
        Integer lastPrice = coin.getPrice().multiply(new BigDecimal(100)).intValue();
        Integer coefficient = 0;
        if (startPriceOf24Hours.compareTo(coin.getPrice().multiply(new BigDecimal(100)).intValue()) > 0) {
            coefficient = lastPrice / startPriceOf24Hours / lastTimeList.size();
        } else if (startPriceOf24Hours.compareTo(coin.getPrice().multiply(new BigDecimal(100)).intValue()) < 0) {
            coefficient = -(startPriceOf24Hours / lastPrice / lastTimeList.size());
        }
        Integer p = startPriceOf24Hours;
        for (int i = 1; i < lastTimeList.size(); i++) {
            CandlestickChart candlestickChart = new CandlestickChart();
            candlestickChart.setTime(timeList.get(i));
            candlestickChart.setCoinId(coin.getId());

            int price = this.randomPrice(p, p * (100 + coefficient) / 100);
            candlestickChart.setPrice(new BigDecimal(price).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP));

            int thisTimeMax = price * 110 / 100;
            int thisTimeMin = price * 90 / 100;

            candlestickChart.setMaxPrice(new BigDecimal(price + MY_RANDOM.nextInt(thisTimeMax - price + 1)).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP));
            candlestickChart.setMinPrice(new BigDecimal(thisTimeMin + MY_RANDOM.nextInt(price - thisTimeMin + 1)).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP));
            candlestickChartList.add(candlestickChart);
            p = price;
        }

//        int max = Math.max(coin.getStartPrice().multiply(new BigDecimal(100)).intValue(), coin.getPrice().multiply(new BigDecimal(100)).intValue()) * 120 / 100;
//        int min = Math.min(coin.getStartPrice().multiply(new BigDecimal(100)).intValue(), coin.getPrice().multiply(new BigDecimal(100)).intValue()) * 80 / 100;
//        for (LocalDateTime time : timeList) {
//            CandlestickChart candlestickChart = new CandlestickChart();
//            candlestickChart.setTime(time);
//            candlestickChart.setCoinId(coin.getId());
//            int price = min + random.nextInt(max - min + 1);
//            candlestickChart.setPrice(new BigDecimal(price).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP));
//
//            int thisTimeMax = price * 110 / 100;
//            int thisTimeMin = price * 90 / 100;
//
//            candlestickChart.setMaxPrice(new BigDecimal(price + random.nextInt(thisTimeMax - price + 1)).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP));
//            candlestickChart.setMinPrice(new BigDecimal(thisTimeMin + random.nextInt(price - thisTimeMin + 1)).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP));
//            candlestickChartList.add(candlestickChart);
//        }
        candlestickChartService.saveBatch(candlestickChartList);
    }

    @Override
    public List<LocalDateTime> generateTimeList(LocalDateTime startTime, LocalDateTime endTime) {
        List<LocalDateTime> list = new ArrayList<>();
        LocalDateTime current = startTime;
        while (current.isBefore(endTime) || current.isEqual(endTime)) {
            list.add(current);
            // Increment by half an hour
            current = current.plusMinutes(30);
        }
        return list;
    }

    public LocalDateTime convertThirtyMinute(LocalDateTime time) {
        time.withSecond(0);
        if (time.getMinute() > 0 && time.getMinute() < 30) {
            time.withMinute(30);
        } else if (time.getMinute() > 30) {
            time.withMinute(0).plusHours(1);
        }
        return time;
    }

    public List<Integer> generate24HoursData(Integer price, int flag) {
        List<Integer> list = new ArrayList<>();

        switch (flag) {
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
            default:
                break;
        }
        return list;
    }

    public Integer randomPrice(Integer price1, Integer price2) {
        Integer max = Math.max(price1, price2);
        Integer min = Math.min(price1, price2);
        return min + MY_RANDOM.nextInt(max - min + 1);
    }
}
