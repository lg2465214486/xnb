package com.example.xnb.controller;

import com.example.xnb.config.AdminSession;
import com.example.xnb.config.JsonResult;
import com.example.xnb.entity.User;
import com.example.xnb.pojo.AddCoinParam;
import com.example.xnb.service.ICoinService;
import com.example.xnb.service.IHoldCoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 *
 * 2024/03/05 9:21 下午
 */

@RestController
@RequestMapping("/coin")
public class CoinController {


    @Autowired
    private ICoinService coinService;
    @Autowired
    private IHoldCoinService holdCoinList;


    @GetMapping("/list")
    public JsonResult coinList(int collect) {
        return new JsonResult(coinService.allList(collect));
    }

    @GetMapping("/hold/list")
    public JsonResult holdCoinList() {
        User user = AdminSession.getInstance().admin();
        if (null == user) {
            return null;
        }
        return new JsonResult(holdCoinList.holdCoinList(user.getId()));
    }

    @GetMapping("/info")
    public JsonResult info(String coinId) {
        return new JsonResult(coinService.info(coinId));
    }
}
