package com.example.xnb.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.xnb.entity.Coin;
import com.example.xnb.mapper.CoinMapper;
import com.example.xnb.pojo.AddCoinParam;
import com.example.xnb.service.AlgorithmService;
import com.example.xnb.service.ICoinService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 *
 * 2024/03/05 9:24 下午
 */

@Service
public class CoinServiceImpl extends ServiceImpl<CoinMapper, Coin> implements ICoinService {

    @Autowired
    private AlgorithmService algorithmService;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addCoin(AddCoinParam param) {
        Coin coin = new Coin();
        UUID uuid = UUID.randomUUID();
        BeanUtils.copyProperties(param, coin);
        coin.setId(uuid.toString().replace("-", ""));
        this.save(coin);
        algorithmService.addCoinGenerateData(coin);
    }
}
