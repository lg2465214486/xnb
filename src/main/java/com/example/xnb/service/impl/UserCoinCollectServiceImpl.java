package com.example.xnb.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.xnb.config.AdminSession;
import com.example.xnb.entity.User;
import com.example.xnb.entity.UserCoinCollect;
import com.example.xnb.mapper.UserCoinCollectMapper;
import com.example.xnb.service.IUserCoinCollectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * 2024/03/05 10:27 下午
 */

@Service
public class UserCoinCollectServiceImpl extends ServiceImpl<UserCoinCollectMapper, UserCoinCollect> implements IUserCoinCollectService {

    @Autowired
    private UserCoinCollectMapper userCoinCollectMapper;

    @Override
    public void collectCoin(String coinId, int i) {
        Integer userId = AdminSession.getInstance().admin().getId();
        if (i == 1) {
            UserCoinCollect userCoinCollect = new UserCoinCollect();
            userCoinCollect.setCoinId(coinId);
            userCoinCollect.setUserId(userId);
            this.save(userCoinCollect);
        } else {
            userCoinCollectMapper.removeCollect(userId, coinId);
        }
    }
}
