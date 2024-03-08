package com.example.xnb.service.impl;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.xnb.config.JsonResult;
import com.example.xnb.entity.CandlestickChart;
import com.example.xnb.mapper.CandlestickChartMapper;
import com.example.xnb.service.AlgorithmService;
import com.example.xnb.service.ICandlestickChartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * 2024/03/05 10:26 下午
 */

@Service
public class CandlestickChartServiceImpl extends ServiceImpl<CandlestickChartMapper, CandlestickChart> implements ICandlestickChartService {


    @Override
    public JsonResult lineList(String coinId, String flag) {
        List<List<Object>> returnList = new ArrayList<>();
        LambdaQueryWrapper<CandlestickChart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CandlestickChart::getCoinId, coinId);
        switch (flag) {
            case "hour" :
                wrapper.like(CandlestickChart::getTime, ":00:00");
                break;
            case "day" :
                wrapper.like(CandlestickChart::getTime, " 00:00:00");
                break;
            case "week" :
                wrapper.eq(CandlestickChart::getWeek, 1).like(CandlestickChart::getTime, " 00:00:00");
                break;
            case "month" :
                wrapper.like(CandlestickChart::getTime, "-01 00:00:00");;
                break;
            default:
                break;
        }
        List<CandlestickChart> list = this.list(wrapper);
        BigDecimal x = new BigDecimal(0);
        for (CandlestickChart c : list) {
            List<Object> line = new ArrayList<>();
            line.add(LocalDateTimeUtil.format(c.getTime(), "yyyy-MM-dd HH:mm:ss"));
            line.add(x.compareTo(BigDecimal.ZERO)>0? x:c.getPrice());
            line.add(c.getPrice());
            line.add(c.getMinPrice());
            line.add(c.getMaxPrice());
            line.add(((long)(new Random().nextInt(15000) + 15000)) * 10000L);
            returnList.add(line);
            x = c.getPrice();
        }
        return new JsonResult(returnList);
    }
}