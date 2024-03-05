package com.example.xnb.controller;


import com.example.xnb.config.AdminSession;
import com.example.xnb.config.JsonResult;
import com.example.xnb.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;
    /**
     * 获取用户信息
     * @return
     */
    @GetMapping("/info")
    public JsonResult userInfo() {
        return new JsonResult(userService.getUserInfo());
    }

    /**
     * 交易记录
     * @return
     */
    @GetMapping("/deal/detail")
    public JsonResult transactionRecord() {
        return new JsonResult(userService.getTransactionRecordById(AdminSession.getInstance().admin().getId()));
    }

}
