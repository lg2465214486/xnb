package com.example.xnb.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.xnb.config.AdminSession;
import com.example.xnb.entity.XNBSystem;
import com.example.xnb.entity.XNBTopUp;
import com.example.xnb.entity.XNBUser;
import com.example.xnb.entity.XNBWithdraw;
import com.example.xnb.mapper.XNBSystemMapper;
import com.example.xnb.mapper.XNBTopUpMapper;
import com.example.xnb.mapper.XNBUserMapper;
import com.example.xnb.mapper.XNBWithdrawMapper;
import com.example.xnb.pojo.ExamineParam;
import com.example.xnb.service.IXNBSystemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class XNBSystemServiceImpl extends ServiceImpl<XNBSystemMapper, XNBSystem> implements IXNBSystemService {

    @Autowired
    private XNBSystemMapper systemMapper;
    @Autowired
    private XNBTopUpMapper topUpMapper;
    @Autowired
    private XNBWithdrawMapper withdrawMapper;
    @Autowired
    private XNBUserMapper userMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String editKeyValue(String key, String value) {
        XNBSystem sys = systemMapper.selectOne(Wrappers.lambdaQuery(XNBSystem.class).eq(XNBSystem::getSysKey, key));
        if (null == sys) {
            sys = new XNBSystem();
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
        XNBTopUp xnbTopUp = topUpMapper.selectById(param.getId());
        xnbTopUp.setUpdatedDate(LocalDateTime.now());
        xnbTopUp.setStatus(param.getStatus());
        topUpMapper.updateById(xnbTopUp);
        return "success";
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String withdrawExamine(ExamineParam param) {
        XNBWithdraw xnbWithdraw = withdrawMapper.selectById(param.getId());
        xnbWithdraw.setUpdatedDate(LocalDateTime.now());
        xnbWithdraw.setStatus(param.getStatus());
        withdrawMapper.updateById(xnbWithdraw);
        XNBUser xnbUser = userMapper.selectById(xnbWithdraw.getUserId());
        if (param.getStatus() == 3)
            xnbUser.setUstd(xnbUser.getUstd().add(xnbWithdraw.getSales()));
            userMapper.updateById(xnbUser);
        if (StrUtil.isNotEmpty(xnbUser.getToken()))
            AdminSession.getInstance().updateAdmin(xnbUser.getToken(),xnbUser);
        return "success";
    }
}
