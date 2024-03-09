package com.example.xnb.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.xnb.entity.HoldCoin;
import com.example.xnb.entity.TradingInfo;
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
public interface IHoldCoinService extends IService<HoldCoin> {


    List<List<Object>> holdCoinList(Integer userId);
}
