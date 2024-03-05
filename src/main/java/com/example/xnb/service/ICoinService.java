package com.example.xnb.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.xnb.entity.Coin;
import com.example.xnb.pojo.AddCoinParam;

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
}
