package com.example.xnb.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.xnb.config.AdminSession;
import com.example.xnb.entity.System;
import com.example.xnb.entity.TopUp;
import com.example.xnb.entity.User;
import com.example.xnb.entity.Withdraw;
import com.example.xnb.mapper.SystemMapper;
import com.example.xnb.mapper.TopUpMapper;
import com.example.xnb.mapper.UserMapper;
import com.example.xnb.mapper.WithdrawMapper;
import com.example.xnb.pojo.ExamineParam;
import com.example.xnb.service.ISystemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author .
 * @since 2023-11-20
 */
@Service
public class SystemServiceImpl extends ServiceImpl<SystemMapper, System> implements ISystemService {

    @Autowired
    private SystemMapper systemMapper;
    @Autowired
    private TopUpMapper topUpMapper;
    @Autowired
    private WithdrawMapper withdrawMapper;
    @Autowired
    private UserMapper userMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String editKeyValue(String key, String value) {
        System sys = systemMapper.selectOne(Wrappers.lambdaQuery(System.class).eq(System::getSysKey, key));
        if (null == sys) {
            sys = new System();
            sys.setSysKey(key);
            sys.setSysValue(value);
            systemMapper.insert(sys);
            return "success";
        }
        sys.setSysValue(value);
        systemMapper.updateById(sys);
        return "success";
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String topUpExamine(ExamineParam param) {
        TopUp topUp = topUpMapper.selectById(param.getId());
        topUp.setUpdatedDate(LocalDateTime.now());
        topUp.setStatus(param.getStatus());
        User u = userMapper.selectById(topUp.getUserId());
        if(topUp.getStatus() == 2){
            switch (topUp.getBz()){
                case "ustd":
                    u.setUstd(u.getUstd().add(topUp.getSales()));
                    break;
                case "btc":
                    u.setBtc(u.getBtc().add(topUp.getSales()));
                    break;
                case "eth":
                    u.setEth(u.getEth().add(topUp.getSales()));
                    break;
            }
        }
        userMapper.updateById(u);
        if (StrUtil.isNotEmpty(u.getToken()))
            AdminSession.getInstance().updateAdmin(u.getToken(),u);
        topUpMapper.updateById(topUp);
        return "success";
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String withdrawExamine(ExamineParam param) {
        Withdraw withdraw = withdrawMapper.selectById(param.getId());
        withdraw.setUpdatedDate(LocalDateTime.now());
        withdraw.setStatus(param.getStatus());
        withdrawMapper.updateById(withdraw);
        User user = userMapper.selectById(withdraw.getUserId());
        if (param.getStatus() == 3)
            user.setUstd(user.getUstd().add(withdraw.getSales()));
            userMapper.updateById(user);
        if (StrUtil.isNotEmpty(user.getToken()))
            AdminSession.getInstance().updateAdmin(user.getToken(),user);
        return "success";
    }
}
