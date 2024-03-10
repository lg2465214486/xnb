package com.example.xnb.controller;

import com.example.xnb.config.JsonResult;
import com.example.xnb.config.JsonResultUtil;
import com.example.xnb.entity.Vip;
import com.example.xnb.mapper.VipMapper;
import com.example.xnb.pojo.*;
import com.example.xnb.service.*;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/qwertyuiop")
public class AdminController {


    @Autowired
    private IUserService userService;
    @Autowired
    private ISystemService systemService;
    @Autowired
    private ITopUpService topUpService;
    @Autowired
    private IUnderwayService underwayService;
    @Autowired
    private IWithdrawService withdrawService;
    @Autowired
    private CommonService commonService;
    @Autowired
    private ICoinService coinService;
    @Autowired
    private ITradingInfoService tradingInfoService;

    /**
     * 用户列表
     * @return
     */
    @GetMapping("/user/all")
    public JsonResult userList(ListParam param) {
        return new JsonResult(userService.findUserList(param));
    }
    /**
     * 禁用
     * @return
     */
    @GetMapping("/user/stop")
    public JsonResult userStatus(String uuid, int isStop) {
        return new JsonResult(userService.stopOrOpenUser(uuid, isStop));
    }

    /**
     * 充值列表
     * @return
     */
    @GetMapping("/topUp/all")
    public JsonResult topUpList(ListParam param) {
        return new JsonResult(topUpService.findTopUpList(param));
    }

    /**
     * 提现列表
     * @return
     */
    @GetMapping("/withdraw/all")
    public JsonResult withdrawList(ListParam param) {
        return new JsonResult(withdrawService.findWithdrawList(param));
    }

    /**
     * 充值审批
     * @return
     */
    @GetMapping("/topUp/examine")
    public JsonResult topUpExamine(ExamineParam param) {
        return new JsonResult(systemService.topUpExamine(param));
    }

    /**
     * 提现审批
     * @return
     */
    @GetMapping("/withdraw/examine")
    public JsonResult withdrawExamine(ExamineParam param) {
        return new JsonResult(systemService.withdrawExamine(param));
    }

    /**
     * 新增用户
     * @param param
     * @return
     */
    @PostMapping("/user/add")
    public JsonResult addUser(@RequestBody UserParam param) {
        return userService.addUser(param);
    }

    /**
     * 修改用户
     * @param param
     * @return
     */
    @PostMapping("/user/edit")
    public JsonResult editUser(@RequestBody UserParam param) {
        return userService.editUser(param);
    }

    @Autowired
    private VipMapper vipMapper;

    /**
     * vip列表
     * @return
     */
    @GetMapping("/user/vipList")
    public JsonResult vipList() {
        return JsonResultUtil.success(vipMapper.selectList(null));
    }

    /**
     * vip列表
     * @return
     */
    @PostMapping("/user/vipEdit")
    public JsonResult vipEdit(@RequestBody VipEditParam vipEditParam) {
        Vip vip = vipMapper.selectById(vipEditParam.getId());
        vip.setRate(vipEditParam.getVipRate());
        vipMapper.updateById(vip);
        return JsonResultUtil.success();
    }

    @Data
    public static class VipEditParam{
        Integer id;
        BigDecimal vipRate;
    }


    /**
     * 系统变量编辑
     * @param param
     * @return
     */
    @PostMapping("/sys/edit")
    public JsonResult edit(@RequestBody SysEditParam param) {
        return new JsonResult(systemService.editKeyValue(param.getKey(), param.getValue()));
    }

    @GetMapping("/coin/list")
    public JsonResult coinList(String keywords, long pageNo, long pageSize) {
        return new JsonResult(coinService.adminAllList(keywords, pageNo, pageSize));
    }


    /**
     * 添加coin
     * @return
     */
    @PostMapping("/add")
    public JsonResult add(@RequestBody AddCoinParam param) {
        coinService.addCoin(param);
        return new JsonResult("success");
    }

    /**
     * 编辑coin
     * @return
     */
    @PostMapping("/edit")
    public JsonResult edit(@RequestBody AddCoinParam param) {
        coinService.editCoin(param);
        return new JsonResult("success");
    }


    /**
     * 设置涨幅
     * @return
     */
    @GetMapping("/setIncrease")
    public JsonResult setIncrease(String coinId, String increase) {
        coinService.setIncrease(coinId, increase);
        return new JsonResult("success");
    }


    /**
     * 删除coin
     * @return
     */
    @GetMapping("/del")
    public JsonResult del(String coinId) {
        coinService.delCoin(coinId);
        return new JsonResult("success");
    }

    @PostMapping("/trading/all")
    public JsonResult allTrading(@RequestBody TradingSelectParam param) {
        return new JsonResult(tradingInfoService.allTrading(param));
    }

}
