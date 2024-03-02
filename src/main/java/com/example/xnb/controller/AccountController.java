package com.example.xnb.controller;


import com.example.xnb.config.JsonResult;
import com.example.xnb.pojo.ConversionParam;
import com.example.xnb.pojo.MoneyOptionParam;
import com.example.xnb.service.IXNBUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private IXNBUserService userService;

    /**
     * 充值
     * @return
     */
    @PostMapping("/topUp")
    public JsonResult topUp(@RequestBody MoneyOptionParam param) {
        return new JsonResult(userService.topUp(param));
    }

    /**
     * 提现
     * @return
     */
    @PostMapping("/withdraw")
    public JsonResult withdraw(@RequestBody MoneyOptionParam param) throws Exception {
        return new JsonResult(userService.withdraw(param));
    }

    /**
     * 闪兑
     * @return
     */
    @PostMapping("/conversion")
    public JsonResult conversion(@RequestBody ConversionParam param) throws Exception{
        return new JsonResult(userService.conversion(param));
    }
}
