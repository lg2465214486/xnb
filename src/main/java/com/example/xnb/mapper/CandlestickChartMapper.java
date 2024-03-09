package com.example.xnb.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.xnb.entity.CandlestickChart;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author .
 * @since 2023-11-20
 */
public interface CandlestickChartMapper extends BaseMapper<CandlestickChart> {

    @Select("select * from candlestick_chart where coin_id=#{coinId} and time < #{now} order by time desc limit 1;")
    CandlestickChart selectLastData(@Param("coinId") String coinId, @Param("now") String now);

    @Select("select max(price) from candlestick_chart where coin_id=#{coinId} and `time` < #{today} and `time` > #{yesterday}")
    BigDecimal maxOf24Hours(@Param("coinId") String coinId, @Param("today") String today, @Param("yesterday") String yesterday);

    @Select("select min(price) from candlestick_chart where coin_id=#{coinId} and `time` < #{today} and `time` > #{yesterday}")
    BigDecimal minOf24Hours(@Param("coinId") String coinId, @Param("today") String today, @Param("yesterday") String yesterday);
}
