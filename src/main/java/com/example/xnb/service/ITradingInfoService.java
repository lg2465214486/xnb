package com.example.xnb.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.xnb.entity.Coin;
import com.example.xnb.entity.TradingInfo;
import com.example.xnb.pojo.AddCoinParam;
import com.example.xnb.pojo.TradingParam;

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
}
