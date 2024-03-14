package com.example.xnb.controller;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.example.xnb.entity.CandlestickChart;
import com.example.xnb.entity.Coin;
import com.example.xnb.mapper.CandlestickChartMapper;
import com.example.xnb.mapper.CoinMapper;
import com.example.xnb.service.AlgorithmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

/**
 *
 * 2024/03/09 2:19 上午
 */

@Component
public class JobService {

    private static final Random MY_RANDOM = new Random();

    @Autowired
    private CoinMapper coinMapper;
    @Autowired
    private CandlestickChartMapper candlestickChartMapper;
    @Autowired
    private AlgorithmService algorithmService;


    @Transactional(rollbackFor = Exception.class)
    @Scheduled(cron = "0 28,58 * * * ? ")
    public void generateDataJob() {
        LocalDateTime nowTime = algorithmService.convertThirtyMinute(LocalDateTime.now());
        String now = LocalDateTimeUtil.format(algorithmService.convertThirtyMinute(LocalDateTime.now()), "yyyy-MM-dd HH:mm:ss");
        List<Coin> list = coinMapper.selectNeedGenerateCoin(now);
        for (Coin coin : list) {
            BigDecimal increase = coin.getIncrease();
            if (ObjectUtils.isEmpty(increase)) {
                increase = new BigDecimal(MY_RANDOM.nextInt(150) + 50).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
                increase = increase.multiply(MY_RANDOM.nextInt(2) == 0 ? new BigDecimal(-1):BigDecimal.ONE);
            }

            CandlestickChart candlestickChart = new CandlestickChart();
            candlestickChart.setTime(nowTime);
            candlestickChart.setWeek(nowTime.getDayOfWeek().getValue());
            candlestickChart.setCoinId(coin.getId());
            BigDecimal price = coin.getPrice().add(coin.getPrice().multiply(increase).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP));
            candlestickChart.setPrice(price);

            int intPrice = price.multiply(new BigDecimal(100)).intValue();
//            int abs = Math.abs(increase.multiply(new BigDecimal(100)).intValue());
//            int thisTimeMax = new BigDecimal(intPrice).multiply(new BigDecimal(100).add(new BigDecimal(abs).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP))).intValue();
//            int thisTimeMin = new BigDecimal(intPrice).multiply(new BigDecimal(100).subtract(new BigDecimal(abs).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP))).intValue();
            int thisTimeMax = intPrice * 110 / 100;
            int thisTimeMin = intPrice * 90 / 100;

            candlestickChart.setMaxPrice(new BigDecimal(intPrice + MY_RANDOM.nextInt(thisTimeMax - intPrice + 1)).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP));
            candlestickChart.setMinPrice(new BigDecimal(thisTimeMin + MY_RANDOM.nextInt(intPrice - thisTimeMin + 1)).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP));

            coin.setPrice(price);
            coinMapper.updateById(coin);
            candlestickChartMapper.insert(candlestickChart);
        }
    }
}
