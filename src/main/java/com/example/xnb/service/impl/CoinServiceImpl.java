package com.example.xnb.service.impl;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.xnb.config.AdminSession;
import com.example.xnb.entity.*;
import com.example.xnb.exception.GlobalException;
import com.example.xnb.mapper.CandlestickChartMapper;
import com.example.xnb.mapper.CoinMapper;
import com.example.xnb.mapper.UserCoinCollectMapper;
import com.example.xnb.pojo.AddCoinParam;
import com.example.xnb.pojo.dto.CoinDto;
import com.example.xnb.pojo.dto.CoinListDto;
import com.example.xnb.service.AlgorithmService;
import com.example.xnb.service.ICandlestickChartService;
import com.example.xnb.service.ICoinService;
import com.example.xnb.service.IHoldCoinService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
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
    @Autowired
    private CandlestickChartMapper candlestickChartsMapper;
    @Autowired
    private CoinMapper coinMapper;
    @Autowired
    private IHoldCoinService holdCoinService;


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
    public List<List<Object>> allList(int collect) {
        List<List<Object>> returnList = new ArrayList<>();
        List<CoinListDto> coinList = null;
        LocalDateTime nowTime = LocalDateTime.now();
        LocalDateTime todayTime = algorithmService.convertBeforeThirtyMinute(nowTime);
        String now = LocalDateTimeUtil.format(todayTime, "yyyy-MM-dd HH:mm:ss");
        String yesterday = LocalDateTimeUtil.format(todayTime.minusDays(1), "yyyy-MM-dd HH:mm:ss");
        if (1 == collect) {
            if (null == AdminSession.getInstance().admin()) {
                return null;
            }
            Integer id = AdminSession.getInstance().admin().getId();
            LambdaQueryWrapper<UserCoinCollect> collectWrapper = new LambdaQueryWrapper<>();
            collectWrapper.eq(UserCoinCollect::getUserId, id);
            List<UserCoinCollect> collects = userCoinCollectMapper.selectList(collectWrapper);
            List<String> coinIds = collects.stream().map(UserCoinCollect::getCoinId).collect(Collectors.toList());
            coinIds.add("aaaaaaaaaa");
            coinList = coinMapper.selectAllList(coinIds, now, yesterday);
        } else {
            coinList = coinMapper.selectAllList(null, now, yesterday);
        }
        for (CoinListDto c : coinList) {
            List<Object> l = new ArrayList<>();
            l.add(c.getId());
            l.add(c.getName());
            l.add(c.getImage());
            l.add(c.getPrice());
            l.add(null == c.getIncrease24Hours()? BigDecimal.ZERO:c.getIncrease24Hours().setScale(2, RoundingMode.DOWN));
            returnList.add(l);
        }
        return returnList;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delCoin(String coinId) {
        Coin c = this.getOne(new LambdaQueryWrapper<Coin>().eq(Coin::getIsDel, 0).eq(Coin::getId, coinId));
        if (ObjectUtils.isEmpty(c)) {
            throw new GlobalException(500, "不存在");
        }
        c.setIsDel(1);
        this.updateById(c);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void setIncrease(String coinId, String increase) {
        Coin c = this.getOne(new LambdaQueryWrapper<Coin>().eq(Coin::getIsDel, 0).eq(Coin::getId, coinId));
        if (ObjectUtils.isEmpty(c)) {
            throw new GlobalException(500, "不存在");
        }
        if (null == increase || "".equals(increase)) {
            coinMapper.setIncreaseNullById(coinId);
        } else {
            c.setIncrease(new BigDecimal(increase));
            this.updateById(c);
        }
    }

    @Override
    public CoinDto info(String coinId) {
        LocalDateTime nowTime = LocalDateTime.now();
        LocalDateTime todayTime = algorithmService.convertBeforeThirtyMinute(nowTime);
        String today = LocalDateTimeUtil.format(todayTime, "yyyy-MM-dd HH:mm:ss");
        String yesterday = LocalDateTimeUtil.format(todayTime.minusDays(1), "yyyy-MM-dd HH:mm:ss");
        CoinDto coinDto = coinMapper.selectInfo(coinId, today, yesterday);
        if (null == coinDto) {
            return null;
        }
        coinDto.setIncrease24Hours(null == coinDto.getIncrease24Hours()? BigDecimal.ZERO:coinDto.getIncrease24Hours().setScale(2, RoundingMode.DOWN));
        
        coinDto.setHigh(candlestickChartsMapper.maxOf24Hours(coinId, today, yesterday));
        coinDto.setLow(candlestickChartsMapper.minOf24Hours(coinId, today, yesterday));
        
        User user = AdminSession.getInstance().admin();
        if (null == user) {
            coinDto.setIsCollect(0);
        } else {
            List<UserCoinCollect> list = userCoinCollectMapper.selectList(new LambdaQueryWrapper<UserCoinCollect>()
                    .eq(UserCoinCollect::getUserId, user.getId())
                    .eq(UserCoinCollect::getCoinId, coinId));
            HoldCoin holdCoin = holdCoinService.getOne(new LambdaQueryWrapper<HoldCoin>()
                    .eq(HoldCoin::getUserId, user.getId())
                    .eq(HoldCoin::getCoinId, coinId));
            if (ObjectUtils.isEmpty(holdCoin)) {
                coinDto.setHoldCount(BigDecimal.ZERO);
            } else {
                coinDto.setHoldCount(holdCoin.getCount()); 
            }
            if (list.size() == 0) {
                coinDto.setIsCollect(0);
            } else {
                coinDto.setIsCollect(1);
            }
        }
        return coinDto;
    }

    @Override
    public Page<Coin> adminAllList(String keywords, long pageNo, long pageSize) {
        Page<Coin> page = new Page<>();
        page.setCurrent(pageNo);
        page.setSize(pageSize);
        LambdaQueryWrapper<Coin> wrapper = new LambdaQueryWrapper<>();
        if (null != keywords)
            wrapper.like(Coin::getName, keywords);
        wrapper.eq(Coin::getIsDel, 0);
        wrapper.orderByDesc(Coin::getCreatedTime);
        return this.page(page, wrapper);
    }

    @Override
    public void editCoin(AddCoinParam param) {
        Coin coin = this.getById(param.getId());
        if (1 == coin.getIsDel()) {
            throw new GlobalException(500, "is deleted");
        }
        BeanUtils.copyProperties(param, coin);
        this.updateById(coin);
    }
}
