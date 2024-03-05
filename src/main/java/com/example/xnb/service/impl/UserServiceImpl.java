package com.example.xnb.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.xnb.config.AdminSession;
import com.example.xnb.config.JsonResult;
import com.example.xnb.entity.TopUp;
import com.example.xnb.entity.User;
import com.example.xnb.entity.Vip;
import com.example.xnb.entity.Withdraw;
import com.example.xnb.mapper.TopUpMapper;
import com.example.xnb.mapper.UserMapper;
import com.example.xnb.mapper.VipMapper;
import com.example.xnb.mapper.WithdrawMapper;
import com.example.xnb.pojo.*;
import com.example.xnb.pojo.dto.DealDetail;
import com.example.xnb.pojo.dto.UserInfo;
import com.example.xnb.service.CommonService;
import com.example.xnb.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.xnb.util.MyDateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author .
 * @since 2023-11-20
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private TopUpMapper topUpMapper;
    @Autowired
    private WithdrawMapper withdrawMapper;
    @Autowired
    private CommonService commonService;
    @Autowired
    private VipMapper vipMapper;

    @Override
    public JsonResult userLogin(LoginParam login) {
        User user = userMapper.selectOne(Wrappers.lambdaQuery(User.class).eq(User::getUuid, login.getLoginName()).or().eq(User::getUserName, login.getLoginName()));
        if (null == user)
            return new JsonResult(500, "Username or password is incorrect.");
        if (user.getIsStop())
            return new JsonResult(500, "Username is stop.");
        if ("admin".equals(user.getUserType())){
            return new JsonResult(500, "Username or password is incorrect.");
        }
        if (!user.getUuid().equals(login.getLoginName()))
            if (!user.getPwd().equals(login.getLoginPassword()))
                return new JsonResult(500, "Username or password is incorrect.");
        String token = AdminSession.getInstance().setAdmin(user);
        user.setToken(token);
        userMapper.updateById(user);
        return new JsonResult(token);
    }

    @Override
    public JsonResult adminUserLogin(LoginParam login) {
        User user = userMapper.selectOne(Wrappers.lambdaQuery(User.class).eq(User::getUuid, login.getLoginName()).or().eq(User::getUserName, login.getLoginName()));
        if (null == user)
            return new JsonResult(500, "Username or password is incorrect.");
        if (user.getIsStop())
            return new JsonResult(500, "Username is stop.");
        if (!"admin".equals(user.getUserType())){
            return new JsonResult(500, "Username or password is incorrect.");
        }
        if (user.getUuid().equals(login.getLoginName()))
            if (!user.getPwd().equals(login.getLoginPassword()))
                return new JsonResult(500, "Username or password is incorrect.");
        String token = AdminSession.getInstance().setAdmin(user);
        user.setToken(token);
        userMapper.updateById(user);
        return new JsonResult(token);
    }

    @Override
    public Page<User> findUserList(ListParam param) {
        Page<User> page = new Page<>();
        page.setCurrent(param.getPageNo());
        page.setSize(param.getPageSize());
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery(User.class);
        if (null != param.getLastKeywords())
            wrapper.like(User::getUserEmail, param.getLastKeywords()).or().like(User::getUserName, param.getLastKeywords());
        if (null != param.getFirstKeywords())
            wrapper.and(i -> i.like(User::getUuid, param.getFirstKeywords()));
        wrapper.ne(User::getUserType,"admin");
        wrapper.orderByDesc(User::getCreatedDate);
        Page<User> userPage = userMapper.selectPage(page, wrapper);
        for (User user : userPage.getRecords()) {
            user.setVipName("VIP0");
            if (ObjectUtil.isNotEmpty(user.getVipGrade())) {
                Vip vip = vipMapper.selectById(user.getVipGrade());
                user.setVipName(vip.getVipName());
            }
        }

        return userPage;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public JsonResult addUser(UserParam param) {
        User user = userMapper.selectOne(Wrappers.lambdaQuery(User.class).eq(User::getUuid, param.getUserName()).or().eq(User::getUserName, param.getUserName()));
        if (null != user)
            throw new RuntimeException("username is use !!");
        if (null == param.getUserName() || null == param.getUserEmail() || null == param.getPhone() || null == param.getPwd())
            throw new RuntimeException("not null !!");
        List<String> uuids = userMapper.findExistUuid();
        //String uuid = MyUtil.getUUID(uuids);
        String uuid = UUID.randomUUID().toString().replace("-", "").toUpperCase();

        User u = new User();
        u.setUuid(uuid);
        u.setIsStop(false);
        u.setCreatedDate(LocalDateTime.now());
        u.setUpdatedDate(LocalDateTime.now());
        u.setUserType("user");
        u.setUserEmail(param.getUserEmail());
        u.setUserName(param.getUserName());
        u.setPhone(param.getPhone());
        u.setPwd(param.getPwd());

        u.setBtc(null == param.getBtc()? BigDecimal.ZERO:new BigDecimal(param.getBtc()));
        u.setEth(null == param.getEth()? BigDecimal.ZERO:new BigDecimal(param.getEth()));
        u.setUstd(null == param.getUstd()? BigDecimal.ZERO:new BigDecimal(param.getUstd()));
        int insert = userMapper.insert(u);
        if (insert != 1)
            return new JsonResult(500, "error");
        return new JsonResult("success");
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public JsonResult editUser(UserParam param) {
        User user = userMapper.selectOne(Wrappers.lambdaQuery(User.class).eq(User::getUuid, param.getUuid()));
        //user.setUserName(param.getUserName());
        //user.setUserEmail(param.getUserEmail());
        //user.setPhone(param.getPhone());
        //user.setPwd(param.getPwd());

        user.setEth(new BigDecimal(param.getEth()));
        user.setBtc(new BigDecimal(param.getBtc()));
        user.setUstd(new BigDecimal(param.getUstd()));
        user.setUstdAds(param.getUstdAds());
        user.setUstdQrCode(param.getUstdQrCode());
        user.setBtcAds(param.getBtcAds());
        user.setBtcQrCode(param.getBtcQrCode());
        user.setEthAds(param.getEthAds());
        user.setEthQrCode(param.getEthQrCode());
        if (ObjectUtil.isNotEmpty(param.getVipGrade())){
            user.setVipGrade(param.getVipGrade());
            if (param.getVipGrade().equals(0)){
                user.setVipGrade(null);
            }
        }
        userMapper.updateById(user);
        if (StrUtil.isNotEmpty(user.getToken()))
            AdminSession.getInstance().updateAdmin(user.getToken(),user);
        return new JsonResult("success");
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String topUp(MoneyOptionParam param) {
        User u = userMapper.selectByUuid(param.getUuid());
        TopUp topUp = new TopUp();
        topUp.setUserId(u.getId());
        topUp.setSales(new BigDecimal(param.getAmount()));
        topUp.setStatus(2);
        topUp.setCreatedDate(LocalDateTime.now());
        topUp.setUpdatedDate(LocalDateTime.now());
        topUp.setBz(param.getBz());
        topUpMapper.insert(topUp);
        switch (param.getBz()){
            case "ustd":
                u.setUstd(u.getUstd().add(new BigDecimal(param.getAmount())));
                break;
            case "btc":
                u.setBtc(u.getBtc().add(new BigDecimal(param.getAmount())));
                break;
            case "eth":
                u.setEth(u.getEth().add(new BigDecimal(param.getAmount())));
                break;
        }
        userMapper.updateById(u);
        if (StrUtil.isNotEmpty(u.getToken()))
            AdminSession.getInstance().updateAdmin(u.getToken(),u);
        return "success";
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String withdraw(MoneyOptionParam param) throws Exception {
        User u = userMapper.selectByUuid(AdminSession.getInstance().admin().getUuid());
        if (u.getUstd().compareTo(new BigDecimal(param.getAmount())) < 0)
            throw new Exception("Insufficient account balance");
        Withdraw withdraw = new Withdraw();
        withdraw.setUserId(u.getId());
        withdraw.setSales(new BigDecimal(param.getAmount()));
        withdraw.setStatus(1);
        withdraw.setTrc20Address(param.getTrc20());
        withdraw.setCreatedDate(LocalDateTime.now());
        withdraw.setUpdatedDate(LocalDateTime.now());
        withdrawMapper.insert(withdraw);
        User user = userMapper.selectById(withdraw.getUserId());
        user.setUstd(user.getUstd().subtract(withdraw.getSales()));
        userMapper.updateById(user);
        return "success";
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String conversion(ConversionParam param) throws Exception {
        AdminSession session = AdminSession.getInstance();
        User user = userMapper.selectById(session.admin().getId());
        String[] option = param.getOption().split("2");
        BigDecimal btc;
        BigDecimal eth;
        try {
            btc = new BigDecimal(commonService.getValueByKey("btc"));
            eth = new BigDecimal(commonService.getValueByKey("eth"));
        }catch (Exception e) {
            throw new Exception("input not number !");
        }
        switch (option[0]) {
            case "ustd":
                if (user.getUstd().compareTo(new BigDecimal(param.getNum())) < 0)
                    throw new Exception("not sufficient funds");
                user.setUstd(user.getUstd().subtract(new BigDecimal(param.getNum())));
                break;
            case "eth":
                if (user.getEth().compareTo(new BigDecimal(param.getNum())) < 0)
                    throw new Exception("not sufficient funds");
                user.setEth(user.getEth().subtract(new BigDecimal(param.getNum())));
                break;
            case "btc":
                if (user.getBtc().compareTo(new BigDecimal(param.getNum())) < 0)
                    throw new Exception("not sufficient funds");
                user.setBtc(user.getBtc().subtract(new BigDecimal(param.getNum())));
                break;
            default: throw new Exception("error");
        }
        switch (option[1]) {
            case "ustd":
                if ("eth".equals(option[0]))
                    user.setUstd(user.getUstd().add(new BigDecimal(param.getNum()).multiply(eth).setScale(4, RoundingMode.HALF_UP)));
                if ("btc".equals(option[0]))
                    user.setUstd(user.getUstd().add(new BigDecimal(param.getNum()).multiply(btc).setScale(4, RoundingMode.HALF_UP)));
                break;
            case "eth":
                user.setEth(user.getEth().add(new BigDecimal(param.getNum()).divide(eth, 4, BigDecimal.ROUND_DOWN)));
                break;
            case "btc":
                user.setBtc(user.getEth().add(new BigDecimal(param.getNum()).divide(btc, 4, BigDecimal.ROUND_DOWN)));
                break;
            default: throw new Exception("error");
        }
        userMapper.updateById(user);
        session.updateAdmin(user);
        return "success";
    }

    @Override
    public String stopOrOpenUser(String uuid, int isStop) {
        User u = userMapper.selectOne(Wrappers.lambdaQuery(User.class).eq(User::getUuid, uuid));
        u.setIsStop(isStop == 1);
        userMapper.updateById(u);
        return "success";
    }

    @Override
    public List<DealDetail> getTransactionRecordById(Integer id) {
        List<DealDetail> details = new ArrayList<>();
        List<Object> dealList = new ArrayList<>();
        List<TopUp> topUps = topUpMapper.selectList(Wrappers.lambdaQuery(TopUp.class).eq(TopUp::getUserId, id));
        List<Withdraw> withdraws = withdrawMapper.selectList(Wrappers.lambdaQuery(Withdraw.class).eq(Withdraw::getUserId, id));
        dealList.addAll(topUps);
        dealList.addAll(withdraws);
        for (Object o : dealList) {
            DealDetail detail = new DealDetail();
            if (o instanceof TopUp) {
                TopUp v = (TopUp) o;
                detail.setType("1");
                detail.setAmount(v.getBz() +"+"+ v.getSales().setScale(4, RoundingMode.HALF_UP).toPlainString());
                detail.setTrc20("");
                detail.setTime(MyDateUtils.dateTimeFormat(v.getCreatedDate()));
                detail.setLocalDateTime(v.getCreatedDate());
                detail.setStatus(v.getStatus()+"");
            } else {
                Withdraw v = (Withdraw) o;
                detail.setType("2");
                detail.setAmount(v.getSales().setScale(4, RoundingMode.HALF_UP).toPlainString());
                detail.setTime(MyDateUtils.dateTimeFormat(v.getCreatedDate()));
                detail.setLocalDateTime(v.getCreatedDate());
                detail.setTrc20(v.getTrc20Address());
                detail.setStatus(v.getStatus()+"");
            }
            details.add(detail);
        }
        Collections.sort(details);
        return details;
    }

    @Override
    public UserInfo getUserInfo() {
        UserInfo userInfo = new UserInfo();
        User user = userMapper.selectById(AdminSession.getInstance().admin().getId());
        userInfo.setVipName("VIP0");
        if (ObjectUtil.isNotEmpty(user.getVipGrade())) {
            Vip vip = vipMapper.selectById(user.getVipGrade());
            userInfo.setVipName(vip.getVipName());
        }
        userInfo.setUserEmail(user.getUserEmail());
        userInfo.setPhone(user.getPhone());
        userInfo.setUserName(user.getUserName());
        userInfo.setUuid(user.getUuid());

        userInfo.setEth(user.getEth().setScale(4, RoundingMode.HALF_UP).toPlainString());
        userInfo.setUstd(user.getUstd().setScale(4, RoundingMode.HALF_UP).toPlainString());
        userInfo.setBtc(user.getBtc().setScale(4, RoundingMode.HALF_UP).toPlainString());
        return userInfo;
    }

}
