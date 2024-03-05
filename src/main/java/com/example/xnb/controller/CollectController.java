package com.example.xnb.controller;

import com.example.xnb.config.JsonResult;
import com.example.xnb.pojo.AddCoinParam;
import com.example.xnb.service.IUserCoinCollectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 *
 * 2024/03/06 12:24 上午
 */

@RestController
@RequestMapping("/collect")
public class CollectController {

    @Autowired
    private IUserCoinCollectService userCoinCollectService;

    @GetMapping("/add")
    public JsonResult addCollect(String coinId) {
        userCoinCollectService.collectCoin(coinId, 1);
        return new JsonResult("success");
    }

    @GetMapping("/del")
    public JsonResult delCoolect(String coinId) {
        userCoinCollectService.collectCoin(coinId, 0);
        return new JsonResult("success");
    }
}
