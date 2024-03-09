package com.example.xnb.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.xnb.config.AdminSession;
import com.example.xnb.entity.Coin;
import com.example.xnb.entity.TradingInfo;
import com.example.xnb.entity.UserCoinCollect;
import com.example.xnb.exception.GlobalException;
import com.example.xnb.mapper.CoinMapper;
import com.example.xnb.mapper.TradingInfoMapper;
import com.example.xnb.mapper.UserCoinCollectMapper;
import com.example.xnb.mapper.UserMapper;
import com.example.xnb.pojo.AddCoinParam;
import com.example.xnb.pojo.TradingParam;
import com.example.xnb.service.AlgorithmService;
import com.example.xnb.service.ICoinService;
import com.example.xnb.service.ITradingInfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 *
 * 2024/03/05 9:24 下午
 */

@Service
public class TradingInfoServiceImpl extends ServiceImpl<TradingInfoMapper, TradingInfo> implements ITradingInfoService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<TradingInfo> info(String coinId, Integer id) {
        LambdaQueryWrapper<TradingInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TradingInfo::getUserId, id);
        wrapper.eq(TradingInfo::getCoinId, coinId);
        wrapper.orderByDesc(TradingInfo::getTime);
        return this.list(wrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void trading(TradingParam param) {

    }
}
