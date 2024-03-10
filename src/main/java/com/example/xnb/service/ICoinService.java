package com.example.xnb.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.xnb.entity.Coin;
import com.example.xnb.pojo.AddCoinParam;
import com.example.xnb.pojo.dto.CoinDto;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author .
 * @since 2023-11-20
 */
public interface ICoinService extends IService<Coin> {

    void addCoin(AddCoinParam param);

    List<List<Object>> allList(int collect);

    void delCoin(String coinId);

    void setIncrease(String coinId, String increase);

    CoinDto info(String coinId);

    Page<Coin> adminAllList(String keywords, long pageNo, long pageSize);

    void editCoin(AddCoinParam param);
}
