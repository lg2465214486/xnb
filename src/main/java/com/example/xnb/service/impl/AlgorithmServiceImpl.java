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
    private static final Integer INCREASE = 10;
    private static final Random MY_RANDOM = new Random();

    @Override
    public void addCoinGenerateData(Coin coin) {
        LocalDateTime now = LocalDateTime.now();
        now = this.convertThirtyMinute(now);
        LocalDateTime startTime = LocalDateTime.now().minusMonths(3);
        startTime = this.convertThirtyMinute(startTime);
        LocalDateTime midTime = LocalDateTime.now().minusDays(1);
        midTime = this.convertThirtyMinute(midTime);

        int max = Math.max(coin.getStartPrice().multiply(new BigDecimal(100)).intValue(), coin.getPrice().multiply(new BigDecimal(100)).intValue()) * (INCREASE + 100) / 100;
        int min = Math.min(coin.getStartPrice().multiply(new BigDecimal(100)).intValue(), coin.getPrice().multiply(new BigDecimal(100)).intValue()) * (100 - INCREASE) / 100;

        List<CandlestickChart> candlestickChartList = new ArrayList<>();
        List<LocalDateTime> timeList = this.generateTimeList(startTime, midTime);
        List<LocalDateTime> lastTimeList = this.generateTimeList(midTime, now);

        Integer startPriceOf24Hours = coin.getStartPrice().multiply(new BigDecimal(100)).intValue();
        List<Integer> datas = null;
        int count=48;
        for (int i = 0; i < timeList.size() - 1; i++) {
            if (count == 48) {
                int flag = MY_RANDOM.nextInt(3);
//                if (startPriceOf24Hours * (DAY_OF_INCREASE + 100) / 100 > max) {
//                    flag = 2;
//                } else if (startPriceOf24Hours * (100 - DAY_OF_INCREASE) / 100 < min) {
//                    flag = 1;
//                }
                datas = this.generate24HoursData(startPriceOf24Hours, flag, max, min);
                startPriceOf24Hours = datas.get(47);
                count=0;
            }
            CandlestickChart candlestickChart = new CandlestickChart();
            candlestickChart.setTime(timeList.get(i));
            candlestickChart.setWeek(timeList.get(i).getDayOfWeek().getValue());
            candlestickChart.setCoinId(coin.getId());
            int price = datas.get(i % 48);
            candlestickChart.setPrice(new BigDecimal(price).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP));

            int thisTimeMax = price * 103 / 100;
            int thisTimeMin = price * 97 / 100;

            candlestickChart.setMaxPrice(new BigDecimal(price + MY_RANDOM.nextInt(thisTimeMax - price + 1)).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP));
            candlestickChart.setMinPrice(new BigDecimal(thisTimeMin + MY_RANDOM.nextInt(price - thisTimeMin + 1)).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP));
            candlestickChartList.add(candlestickChart);
            count++;
        }
        Integer lastPrice = coin.getPrice().multiply(new BigDecimal(100)).intValue();
//        Integer coefficient = 0;
//        if (startPriceOf24Hours.compareTo(coin.getPrice().multiply(new BigDecimal(100)).intValue()) > 0) {
//            coefficient = -((startPriceOf24Hours - lastPrice) / lastTimeList.size());
//        } else if (startPriceOf24Hours.compareTo(coin.getPrice().multiply(new BigDecimal(100)).intValue()) < 0) {
//            coefficient = (lastPrice - startPriceOf24Hours) / lastTimeList.size();
//        }
        int[] sss = this.generateBalancedArray(lastPrice - startPriceOf24Hours);
        Integer p = startPriceOf24Hours;
        for (int i = 0; i < lastTimeList.size(); i++) {
            CandlestickChart candlestickChart = new CandlestickChart();
            candlestickChart.setTime(lastTimeList.get(i));
            candlestickChart.setWeek(lastTimeList.get(i).getDayOfWeek().getValue());
            candlestickChart.setCoinId(coin.getId());

//            int price = this.randomPrice(p, p + coefficient);
            if (i == (lastTimeList.size() - 1)) {
                candlestickChart.setPrice(coin.getPrice());
            } else {
                candlestickChart.setPrice(new BigDecimal(p + sss[i]).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP));
            }
            int price = candlestickChart.getPrice().multiply(new BigDecimal(100)).intValue();
            int thisTimeMax = price * 102 / 100;
            int thisTimeMin = price * 98 / 100;

            candlestickChart.setMaxPrice(new BigDecimal(price + MY_RANDOM.nextInt(thisTimeMax - price + 1)).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP));
            candlestickChart.setMinPrice(new BigDecimal(thisTimeMin + MY_RANDOM.nextInt(price - thisTimeMin + 1)).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP));
            candlestickChartList.add(candlestickChart);
            p = price;
        }
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

    @Override
    public LocalDateTime convertThirtyMinute(LocalDateTime time) {
        time = time.withSecond(0);
        if (time.getMinute() > 0 && time.getMinute() < 30) {
            time = time.withMinute(30);
        } else if (time.getMinute() > 30) {
            time = time.withMinute(0).plusHours(1);
        }
        return time;
    }

    public List<Integer> generate24HoursData(Integer price, int flag, Integer max, Integer min) {
        List<Integer> list = new ArrayList<>();

        int ran = 12 + MY_RANDOM.nextInt(23);
        for (int i = 0; i < 48; i++) {
            if (i%ran == 0) {
                flag = MY_RANDOM.nextInt(3);
            }
            switch (flag) {
                case 0:
                    price = price * 97 / 100 + MY_RANDOM.nextInt(price * 6 / 100 + 1);
                    list.add(price);
                    break;
                case 1:
                    price = price + MY_RANDOM.nextInt(price * 3 / 100 + 1);
                    list.add(price);
                    break;
                case 2:
                    price = price * 97 / 100 + MY_RANDOM.nextInt(price * 3 / 100 + 1);
                    list.add(price);
                    break;
                default:
                    break;
            }
            if (price > max) {
                flag = 2;
            } else if (price < min) {
                flag = 1;
            }
        }
        return list;
    }

    public Integer randomPrice(Integer price1, Integer price2) {
        if (price1 < price2) {
            return price1 + MY_RANDOM.nextInt((price2 - price1) * 2 + 1);
        } else {
            return price1 - MY_RANDOM.nextInt((price1 - price2) * 2 + 1);
        }
    }

    public int[] generateBalancedArray(int targetSum) {
        int[] result = new int[48];
        Random random = new Random();
        int currentSum = 0;

        // 初始化数组，确保有正有负数
        for (int i = 0; i < result.length; i++) {
            // 交替放置正负数，确保之间差值在1到10之间
            int value = (i % 2 == 0 ? 1 : -1) * (1 + random.nextInt(10));
            result[i] = value;
            currentSum += value;
        }

        // 调整以匹配目标和
        int index = 0;
        while (currentSum != targetSum) {
            // 找到可以增加或减少的值，以逼近目标和，同时保持正负数和差值条件
            int adjustment = Math.min(Math.abs(targetSum - currentSum), 10);
            if (currentSum < targetSum) {
                // 如果当前和小于目标，增加某个正数或减少一个负数
                if (result[index] > 0) {
                    result[index] += adjustment;
                } else if (result[index] - adjustment >= -10) { // 确保不会超过负数的最大差值限制
                    result[index] -= adjustment;
                }
            } else {
                // 如果当前和大于目标，减少某个正数或增加一个负数
                if (result[index] < 0) {
                    result[index] -= adjustment;
                } else if (result[index] + adjustment <= 10) { // 确保不会超过正数的最大差值限制
                    result[index] += adjustment;
                }
            }
            currentSum = 0;
            for (int value : result) {
                currentSum += value;
            }
            index = (index + 1) % result.length;
        }

        return result;
    }
}
