package com.example.xnb.controller;

import com.example.xnb.config.JsonResult;
import com.example.xnb.service.ICandlestickChartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * 2024/03/09 12:29 上午
 */

@RestController
@RequestMapping("/line")
public class LineController {

    @Autowired
    private ICandlestickChartService candlestickChartService;

    @GetMapping("/all")
    public JsonResult userList(String coinId, String flag) {
        return candlestickChartService.lineList(coinId, flag);
    }
}
