package com.example.xnb.controller;

import com.example.xnb.config.AdminSession;
import com.example.xnb.config.JsonResult;
import com.example.xnb.entity.User;
import com.example.xnb.pojo.TradingParam;
import com.example.xnb.pojo.TradingSelectParam;
import com.example.xnb.service.ITradingInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.web.bind.annotation.*;

/**
 *
 * 2024/03/09 2:11 下午
 */

@RestController
@RequestMapping("/trading")
public class TradingController {

    @Autowired
    private ITradingInfoService tradingInfoService;

    @PostMapping("/trading")
    public JsonResult trading(@RequestBody TradingParam param) {
        param.setUserId(AdminSession.getInstance().admin().getId());
        tradingInfoService.trading(param);
        return new JsonResult("success");
    }

    @PostMapping("/all")
    public JsonResult allTrading(@RequestBody TradingSelectParam param) {
        return new JsonResult(tradingInfoService.allTrading(param));
    }

    @GetMapping("/info")
    public JsonResult info(String coinId) {
        User user = AdminSession.getInstance().admin();
        if (null == user)
            return null;
        return new JsonResult(tradingInfoService.info(coinId, user.getId()));
    }
}
