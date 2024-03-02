package com.example.xnb.controller;


import com.example.xnb.config.AdminSession;
import com.example.xnb.config.JsonResult;
import com.example.xnb.pojo.LoginParam;
import com.example.xnb.service.CommonService;
import com.example.xnb.service.IXNBUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pub")
public class LoginController {

    @Autowired
    private IXNBUserService xnbUserService;
    @Autowired
    private CommonService commonService;

    /**
     * 用户登录
     * @param login
     * @return
     */
    @PostMapping("/user/login")
    public JsonResult login(@RequestBody LoginParam login) {
        return xnbUserService.userLogin(login);
    }

    /**
     * 用户登录
     * @param login
     * @return
     */
    @PostMapping("/user/adminLogin")
    public JsonResult adminLogin(@RequestBody LoginParam login) {
        return xnbUserService.adminUserLogin(login);
    }

    /**
     * 用户登出
     * @return
     */
    @PostMapping("/user/logout")
    public JsonResult logout() {
        return new JsonResult(AdminSession.getInstance().logout());
    }

    /**
     * 获取值
     */
    @GetMapping("/getValue")
    public JsonResult getValue(String name) {
        return new JsonResult(commonService.getValueByKey(name));
    }

}
