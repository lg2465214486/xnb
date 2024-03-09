package com.example.xnb.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.xnb.config.AdminSession;
import com.example.xnb.entity.Coin;
import com.example.xnb.entity.UserCoinCollect;
import com.example.xnb.exception.GlobalException;
import com.example.xnb.mapper.CoinMapper;
import com.example.xnb.mapper.UserCoinCollectMapper;
import com.example.xnb.pojo.AddCoinParam;
import com.example.xnb.service.AlgorithmService;
import com.example.xnb.service.ICoinService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 *
 * 2024/03/05 9:24 下午
 */

@Service
public class CoinServiceImpl extends ServiceImpl<CoinMapper, Coin> implements ICoinService {

    @Autowired
    private AlgorithmService algorithmService;
    @Autowired
    private UserCoinCollectMapper userCoinCollectMapper;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addCoin(AddCoinParam param) {
        if (this.list(new LambdaQueryWrapper<Coin>().eq(Coin::getIsDel, 0).eq(Coin::getName, param.getName())).size() > 0) {
            throw new GlobalException(500, "已存在");
        }
        Coin coin = new Coin();
        UUID uuid = UUID.randomUUID();
        BeanUtils.copyProperties(param, coin);
        coin.setId(uuid.toString().replace("-", ""));
        this.save(coin);
        algorithmService.addCoinGenerateData(coin);
    }

    @Override
    public List<Coin> allList(int collect) {
        LambdaQueryWrapper<Coin> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Coin::getIsDel, 0);
        if (1 == collect) {
            Integer id = AdminSession.getInstance().admin().getId();
            LambdaQueryWrapper<UserCoinCollect> collectWrapper = new LambdaQueryWrapper<>();
            collectWrapper.eq(UserCoinCollect::getUserId, id);
            List<UserCoinCollect> collects = userCoinCollectMapper.selectList(collectWrapper);
            wrapper.in(Coin::getId, collects.stream().map(UserCoinCollect::getCoinId).collect(Collectors.toList()));
            return this.list(wrapper);
        } else {
            return this.list();
        }
    }

    @Override
    public void delCoin(String coinId) {
        Coin c = this.getOne(new LambdaQueryWrapper<Coin>().eq(Coin::getIsDel, 0).eq(Coin::getId, coinId));
        if (ObjectUtils.isEmpty(c)) {
            throw new GlobalException(500, "不存在");
        }
        c.setIsDel(1);
        this.updateById(c);
    }

    @Override
    public void setIncrease(String coinId, int increase) {
        Coin c = this.getOne(new LambdaQueryWrapper<Coin>().eq(Coin::getIsDel, 0).eq(Coin::getId, coinId));
        if (ObjectUtils.isEmpty(c)) {
            throw new GlobalException(500, "不存在");
        }
        c.setIncrease(new BigDecimal(increase).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP));
        this.updateById(c);
    }
}
