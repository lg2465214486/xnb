package com.example.xnb.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.xnb.config.AdminSession;
import com.example.xnb.entity.*;
import com.example.xnb.exception.GlobalException;
import com.example.xnb.mapper.HoldCoinMapper;
import com.example.xnb.mapper.TradingInfoMapper;
import com.example.xnb.mapper.UserMapper;
import com.example.xnb.pojo.TradingParam;
import com.example.xnb.pojo.TradingSelectParam;
import com.example.xnb.pojo.dto.TradingSelectDto;
import com.example.xnb.service.ICoinService;
import com.example.xnb.service.ITradingInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * 2024/03/05 9:24 下午
 */

@Service
public class TradingInfoServiceImpl extends ServiceImpl<TradingInfoMapper, TradingInfo> implements ITradingInfoService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ICoinService coinService;
    @Autowired
    private HoldCoinMapper holdCoinMapper;
    @Autowired
    private TradingInfoMapper tradingInfoMapper;

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
        LocalDateTime now = LocalDateTime.now();
        User user = userMapper.selectById(AdminSession.getInstance().admin().getId());
        if (ObjectUtils.isEmpty(user)) {
            throw new GlobalException(500, "session expired");
        }
        Coin coin = coinService.getOne(new LambdaQueryWrapper<Coin>().eq(Coin::getId, param.getCoinId()).eq(Coin::getIsDel, 0));
        if (ObjectUtils.isEmpty(coin)) {
            throw new GlobalException(500, "coin not exist");
        }
        HoldCoin holdCoin = holdCoinMapper.selectOne(new LambdaQueryWrapper<HoldCoin>()
                .eq(HoldCoin::getUserId, user.getId())
                .eq(HoldCoin::getCoinId, param.getCoinId()));

        BigDecimal price = coin.getPrice().multiply(param.getCount());
        TradingInfo tradingInfo = new TradingInfo();
        if (param.getStatus() == 1) {
            if (coin.getPrice().multiply(param.getCount()).compareTo(user.getUstd()) > 0) {
                throw new GlobalException(500, "not sufficient funds");
            }

            if (ObjectUtils.isEmpty(holdCoin)) {
                holdCoin = new HoldCoin();
                holdCoin.setUserId(user.getId());
                holdCoin.setCoinId(coin.getId());
                holdCoin.setCount(param.getCount());
                holdCoin.setPrice(price);
                holdCoinMapper.insert(holdCoin);
            } else {
                holdCoin.setCount(holdCoin.getCount().add(param.getCount()).setScale(2, RoundingMode.HALF_UP));
                holdCoin.setPrice(holdCoin.getPrice().add(price).setScale(2, RoundingMode.HALF_UP));
                holdCoinMapper.updateById(holdCoin);
            }
            user.setUstd(user.getUstd().subtract(price).setScale(2, RoundingMode.HALF_UP));
        } else {
            if (ObjectUtils.isEmpty(holdCoin)) {
                throw new GlobalException(500, "coin not hold");
            }
            if (param.getCount().compareTo(holdCoin.getCount()) > 0) {
                throw new GlobalException(500, "hold coi not enough");
            }
            holdCoin.setCount(holdCoin.getCount().subtract(param.getCount()).setScale(2, RoundingMode.HALF_UP));
            holdCoin.setPrice(holdCoin.getPrice().subtract(price).setScale(2, RoundingMode.HALF_UP));
            holdCoinMapper.updateById(holdCoin);
            user.setUstd(user.getUstd().add(price).setScale(2, RoundingMode.HALF_UP));
        }
        tradingInfo.setUserId(user.getId());
        tradingInfo.setCoinId(coin.getId());
        tradingInfo.setPrice(price);
        tradingInfo.setCount(param.getCount());
        tradingInfo.setSinglePrice(coin.getPrice());
        tradingInfo.setStatus(param.getStatus());
        tradingInfo.setTime(now);

        userMapper.updateById(user);
        this.save(tradingInfo);
    }

    @Override
    public Page<TradingSelectDto> allTrading(TradingSelectParam param) {
        Page<TradingSelectDto> page = new Page<>();
        page.setCurrent(param.getPageNo());
        page.setSize(param.getPageSize());
        Page<TradingSelectDto> pages = tradingInfoMapper.selectAllTradingList(page, param);

        Map<Integer, User> users = userMapper.selectList(new QueryWrapper<>()).stream().collect(Collectors.toMap(User::getId, o -> o));
        Map<String, Coin> coins = coinService.list().stream().collect(Collectors.toMap(Coin::getId, o -> o));

        for (TradingSelectDto r : pages.getRecords()) {
            r.setCoin(coins.get(r.getCoinId()));
            r.setUser(users.get(r.getUserId()));
        }

        return page;
    }
}
