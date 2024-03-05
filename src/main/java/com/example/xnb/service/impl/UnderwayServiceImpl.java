package com.example.xnb.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.xnb.config.AdminSession;
import com.example.xnb.entity.Underway;
import com.example.xnb.entity.User;
import com.example.xnb.entity.Vip;
import com.example.xnb.mapper.UnderwayMapper;
import com.example.xnb.mapper.UserMapper;
import com.example.xnb.mapper.VipMapper;
import com.example.xnb.pojo.MiningParam;
import com.example.xnb.pojo.dto.Earnings;
import com.example.xnb.service.CommonService;
import com.example.xnb.service.IUnderwayService;
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
public class UnderwayServiceImpl extends ServiceImpl<UnderwayMapper, Underway> implements IUnderwayService {

    @Autowired
    private UnderwayMapper underwayMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CommonService commonService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String start(MiningParam param) throws Exception{
        AdminSession session = AdminSession.getInstance();
        User user = userMapper.selectById(session.admin().getId());
        if (user.getUstd().compareTo(new BigDecimal(param.getMoneyQuantity())) < 0)
            throw new Exception("not sufficient funds");
        user.setUstd(user.getUstd().subtract(new BigDecimal(param.getMoneyQuantity())));
        List<Underway> underways = underwayMapper.selectList(Wrappers.lambdaQuery(Underway.class)
                .eq(Underway::getUserId, user.getId()).eq(Underway::getStatus, 1));
        for (Underway underway : underways) {
            this.stopUnderwayByEntity(underway, user);
        }
        Underway underway = new Underway();
        underway.setUserId(user.getId());
        underway.setMoneyQuantity(new BigDecimal(param.getMoneyQuantity()));
        underway.setEarnings(BigDecimal.ZERO);
        underway.setStatus(1);
        underway.setStartDate(LocalDateTime.now());
        underway.setEndDate(null);
        underway.setCreatedDate(LocalDateTime.now());
        underway.setUpdatedDate(LocalDateTime.now());
        
        userMapper.updateById(user);
        session.updateAdmin(user);
        underwayMapper.insert(underway);
        return "success";
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String stop() {
        AdminSession session = AdminSession.getInstance();
        User user = userMapper.selectById(session.admin().getId());
        List<Underway> underways = underwayMapper.selectList(Wrappers.lambdaQuery(Underway.class)
                .eq(Underway::getUserId, user.getId()).eq(Underway::getStatus, 1));
        BigDecimal earnings = BigDecimal.ZERO;
        for (Underway underway : underways) {
            earnings = earnings.add(this.stopUnderwayByEntity(underway, user)).add(underway.getMoneyQuantity());
        }

        user.setUstd(user.getUstd().add(earnings));
        userMapper.updateById(user);
        session.updateAdmin(user);
        return "success";
    }

    //private BigDecimal stopUnderwayById(Integer id) {
    //    Underway underway = underwayMapper.selectById(id);
    //    return this.stopUnderwayByEntity(underway);
    //}

    private BigDecimal stopUnderwayByEntity(Underway underway, User user) {
        LocalDateTime now = LocalDateTime.now();
        underway.setStatus(2);
        underway.setEarnings(this.earningsCoefficient(underway.getStartDate(), now, underway.getMoneyQuantity(), user));
        underway.setEndDate(now);
        underway.setUpdatedDate(now);
        underwayMapper.updateById(underway);
        return underway.getEarnings();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Earnings findEarnings() {
        User u = AdminSession.getInstance().admin();
        Underway underway = underwayMapper.selectOne(Wrappers.lambdaQuery(Underway.class)
                .eq(Underway::getUserId, u.getId())
                .eq(Underway::getStatus, 1));
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
    private VipMapper vipMapper;

    private BigDecimal earningsCoefficient(LocalDateTime start, LocalDateTime end, BigDecimal sales, User user) {
        //获取秒数
        long nowSecond = start.toEpochSecond(ZoneOffset.ofHours(0));
        long endSecond = end.toEpochSecond(ZoneOffset.ofHours(0));
        long absSeconds = Math.abs(nowSecond - endSecond);
        BigDecimal myRate = new BigDecimal(commonService.getValueByKey("myRate"));
        if (ObjectUtil.isNotEmpty(user.getVipGrade())){
            Vip vip = vipMapper.selectById(user.getVipGrade());
            myRate = vip.getRate();
        }
        BigDecimal daySales = sales.multiply(myRate).setScale(4,BigDecimal.ROUND_HALF_UP);
        BigDecimal secondSales = daySales.divide(BigDecimal.valueOf(86400), 4,BigDecimal.ROUND_HALF_UP);
        return secondSales.multiply(BigDecimal.valueOf(absSeconds)).setScale(4,BigDecimal.ROUND_HALF_UP);
    }
}
