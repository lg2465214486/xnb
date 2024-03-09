package com.example.xnb.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.xnb.entity.Coin;
import com.example.xnb.entity.HoldCoin;
import com.example.xnb.mapper.HoldCoinMapper;
import com.example.xnb.mapper.UserMapper;
import com.example.xnb.service.ICoinService;
import com.example.xnb.service.IHoldCoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * 2024/03/05 9:24 下午
 */

@Service
public class HoldCoinServiceImpl extends ServiceImpl<HoldCoinMapper, HoldCoin> implements IHoldCoinService {

    @Autowired
    private ICoinService coinService;


    @Override
    public List<List<Object>> holdCoinList(Integer userId) {
        List<List<Object>> returnList = new ArrayList<>();
        List<HoldCoin> list = this.list(new LambdaQueryWrapper<HoldCoin>().eq(HoldCoin::getUserId, userId));
        Map<String, Coin> coinMap = coinService.list().stream().collect(Collectors.toMap(Coin::getId, o -> o));
        for (HoldCoin h : list) {
            List<Object> l = new ArrayList<>();
            l.add(coinMap.get(h.getCoinId()).getName());
            l.add(coinMap.get(h.getCoinId()).getImage());
            l.add(h.getCount());
            l.add(h.getCount().multiply(coinMap.get(h.getCoinId()).getPrice()).divide(h.getPrice(), 2, RoundingMode.HALF_UP).subtract(new BigDecimal(100)));
            returnList.add(l);
        }
        return returnList;
    }
}
