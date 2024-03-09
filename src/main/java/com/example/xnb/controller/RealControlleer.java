package com.example.xnb.controller;

import com.example.xnb.config.AdminSession;
import com.example.xnb.config.JsonResult;
import com.example.xnb.pojo.RealParam;
import com.example.xnb.service.IRealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 *
 * 2024/03/09 4:48 下午
 */

@RestController
@RequestMapping("/real")
public class RealControlleer {

    @Autowired
    private IRealService realService;

    @GetMapping("/userInfo")
    public JsonResult info() {
        Integer userId = AdminSession.getInstance().admin().getId();
        return new JsonResult(realService.info(userId));
    }

    @GetMapping("/adminInfo")
    public JsonResult info(Integer userId) {
        return new JsonResult(realService.info(userId));
    }

    @PostMapping("/add")
    public JsonResult userList(@RequestBody RealParam param) {
        Integer userId = AdminSession.getInstance().admin().getId();
        return new JsonResult(realService.add(userId, param));
    }
}
