package com.example.xnb.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.xnb.entity.Coin;
import com.example.xnb.entity.TradingInfo;
import com.example.xnb.pojo.TradingSelectParam;
import com.example.xnb.pojo.dto.TradingSelectDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author .
 * @since 2023-11-20
 */
public interface TradingInfoMapper extends BaseMapper<TradingInfo> {


    Page<TradingSelectDto> selectAllTradingList(@Param("page") Page<TradingSelectDto> page, @Param("param") TradingSelectParam param);
}
