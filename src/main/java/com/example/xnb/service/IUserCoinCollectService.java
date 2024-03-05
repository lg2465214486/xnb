package com.example.xnb.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.xnb.entity.UserCoinCollect;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author .
 * @since 2023-11-20
 */
public interface IUserCoinCollectService extends IService<UserCoinCollect> {

    void collectCoin(String coinId, int i);
}
