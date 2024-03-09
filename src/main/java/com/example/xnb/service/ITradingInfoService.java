package com.example.xnb.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.xnb.entity.Coin;
import com.example.xnb.entity.TradingInfo;
import com.example.xnb.pojo.AddCoinParam;
import com.example.xnb.pojo.TradingParam;
import com.example.xnb.pojo.TradingSelectParam;
import com.example.xnb.pojo.dto.TradingSelectDto;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author .
 * @since 2023-11-20
 */
public interface ITradingInfoService extends IService<TradingInfo> {

    List<TradingInfo> info(String coinId, Integer id);

    void trading(TradingParam param);

    Page<TradingSelectDto> allTrading(TradingSelectParam param);
}
