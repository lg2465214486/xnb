package com.example.xnb.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.xnb.config.AdminSession;
import com.example.xnb.entity.XNBUnderway;
import com.example.xnb.entity.XNBUser;
import com.example.xnb.entity.XNBVip;
import com.example.xnb.mapper.XNBUnderwayMapper;
import com.example.xnb.mapper.XNBUserMapper;
import com.example.xnb.mapper.XNBVipMapper;
import com.example.xnb.pojo.MiningParam;
import com.example.xnb.pojo.dto.Earnings;
import com.example.xnb.service.CommonService;
import com.example.xnb.service.IXNBUnderwayService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author .
 * @since 2023-11-20
 */
@Service
public class XNBUnderwayServiceImpl extends ServiceImpl<XNBUnderwayMapper, XNBUnderway> implements IXNBUnderwayService {

    @Autowired
    private XNBUnderwayMapper underwayMapper;
    @Autowired
    private XNBUserMapper userMapper;
    @Autowired
    private CommonService commonService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String start(MiningParam param) throws Exception{
        AdminSession session = AdminSession.getInstance();
        XNBUser user = userMapper.selectById(session.admin().getId());
        if (user.getUstd().compareTo(new BigDecimal(param.getMoneyQuantity())) < 0)
            throw new Exception("not sufficient funds");
        user.setUstd(user.getUstd().subtract(new BigDecimal(param.getMoneyQuantity())));
        List<XNBUnderway> underways = underwayMapper.selectList(Wrappers.lambdaQuery(XNBUnderway.class)
                .eq(XNBUnderway::getUserId, user.getId()).eq(XNBUnderway::getStatus, 1));
        for (XNBUnderway underway : underways) {
            this.stopUnderwayByEntity(underway, user);
        }
        XNBUnderway xnbUnderway = new XNBUnderway();
        xnbUnderway.setUserId(user.getId());
        xnbUnderway.setMoneyQuantity(new BigDecimal(param.getMoneyQuantity()));
        xnbUnderway.setEarnings(BigDecimal.ZERO);
        xnbUnderway.setStatus(1);
        xnbUnderway.setStartDate(LocalDateTime.now());
        xnbUnderway.setEndDate(null);
        xnbUnderway.setCreatedDate(LocalDateTime.now());
        xnbUnderway.setUpdatedDate(LocalDateTime.now());
        
        userMapper.updateById(user);
        session.updateAdmin(user);
        underwayMapper.insert(xnbUnderway);
        return "success";
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String stop() {
        AdminSession session = AdminSession.getInstance();
        XNBUser user = userMapper.selectById(session.admin().getId());
        List<XNBUnderway> underways = underwayMapper.selectList(Wrappers.lambdaQuery(XNBUnderway.class)
                .eq(XNBUnderway::getUserId, user.getId()).eq(XNBUnderway::getStatus, 1));
        BigDecimal earnings = BigDecimal.ZERO;
        for (XNBUnderway underway : underways) {
            earnings = earnings.add(this.stopUnderwayByEntity(underway, user)).add(underway.getMoneyQuantity());
        }

        user.setUstd(user.getUstd().add(earnings));
        userMapper.updateById(user);
        session.updateAdmin(user);
        return "success";
    }

    //private BigDecimal stopUnderwayById(Integer id) {
    //    XNBUnderway underway = underwayMapper.selectById(id);
    //    return this.stopUnderwayByEntity(underway);
    //}

    private BigDecimal stopUnderwayByEntity(XNBUnderway underway, XNBUser xnbUser) {
        LocalDateTime now = LocalDateTime.now();
        underway.setStatus(2);
        underway.setEarnings(this.earningsCoefficient(underway.getStartDate(), now, underway.getMoneyQuantity(), xnbUser));
        underway.setEndDate(now);
        underway.setUpdatedDate(now);
        underwayMapper.updateById(underway);
        return underway.getEarnings();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Earnings findEarnings() {
        XNBUser u = AdminSession.getInstance().admin();
        XNBUnderway underway = underwayMapper.selectOne(Wrappers.lambdaQuery(XNBUnderway.class)
                .eq(XNBUnderway::getUserId, u.getId())
                .eq(XNBUnderway::getStatus, 1));
        if (null == underway)
            return null;
        underway.setEarnings(this.earningsCoefficient(underway.getStartDate(), LocalDateTime.now(), underway.getMoneyQuantity(), u));
        underwayMapper.updateById(underway);
        Earnings earnings = new Earnings();
        earnings.setEarnings(underway.getEarnings().setScale(4, RoundingMode.HALF_UP).toPlainString());
        earnings.setMoneyQuantity(underway.getMoneyQuantity().setScale(4, RoundingMode.HALF_UP).toPlainString());
        return earnings;
    }

    @Autowired
    private XNBVipMapper xnbVipMapper;

    private BigDecimal earningsCoefficient(LocalDateTime start, LocalDateTime end, BigDecimal sales, XNBUser user) {
        //获取秒数
        long nowSecond = start.toEpochSecond(ZoneOffset.ofHours(0));
        long endSecond = end.toEpochSecond(ZoneOffset.ofHours(0));
        long absSeconds = Math.abs(nowSecond - endSecond);
        BigDecimal myRate = new BigDecimal(commonService.getValueByKey("myRate"));
        if (ObjectUtil.isNotEmpty(user.getVipGrade())){
            XNBVip xnbVip = xnbVipMapper.selectById(user.getVipGrade());
            myRate = xnbVip.getXnbRate();
        }
        BigDecimal daySales = sales.multiply(myRate).setScale(4,BigDecimal.ROUND_HALF_UP);
        BigDecimal secondSales = daySales.divide(BigDecimal.valueOf(86400), 4,BigDecimal.ROUND_HALF_UP);
        return secondSales.multiply(BigDecimal.valueOf(absSeconds)).setScale(4,BigDecimal.ROUND_HALF_UP);
    }
}
