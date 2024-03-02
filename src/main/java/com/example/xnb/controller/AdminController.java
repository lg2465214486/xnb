package com.example.xnb.controller;

import com.example.xnb.config.JsonResult;
import com.example.xnb.config.JsonResultUtil;
import com.example.xnb.entity.XNBVip;
import com.example.xnb.mapper.XNBVipMapper;
import com.example.xnb.pojo.ExamineParam;
import com.example.xnb.pojo.SysEditParam;
import com.example.xnb.pojo.ListParam;
import com.example.xnb.pojo.UserParam;
import com.example.xnb.service.*;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/qwertyuiop")
public class AdminController {


    @Autowired
    private IXNBUserService userService;
    @Autowired
    private IXNBSystemService systemService;
    @Autowired
    private IXNBTopUpService topUpService;
    @Autowired
    private IXNBUnderwayService underwayService;
    @Autowired
    private IXNBWithdrawService withdrawService;
    @Autowired
    private CommonService commonService;
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
    private XNBVipMapper xnbVipMapper;

    /**
     * vip列表
     * @return
     */
    @GetMapping("/user/vipList")
    public JsonResult vipList() {
        return JsonResultUtil.success(xnbVipMapper.selectList(null));
    }

    /**
     * vip列表
     * @return
     */
    @PostMapping("/user/vipEdit")
    public JsonResult vipEdit(@RequestBody VipEditParam vipEditParam) {
        XNBVip xnbVip = xnbVipMapper.selectById(vipEditParam.getId());
        xnbVip.setXnbRate(vipEditParam.getVipRate());
        xnbVipMapper.updateById(xnbVip);
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

}
