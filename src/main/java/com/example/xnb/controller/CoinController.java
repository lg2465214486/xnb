package com.example.xnb.controller;

import com.example.xnb.config.JsonResult;
import com.example.xnb.pojo.AddCoinParam;
import com.example.xnb.service.ICoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * 2024/03/05 9:21 下午
 */

@RestController
@RequestMapping("/coin")
public class CoinController {


    @Autowired
    private ICoinService coinService;

    /**
     * 添加coin
     * @return
     */
    @PostMapping("/add")
    public JsonResult adminLogin(@RequestBody AddCoinParam param) {
        coinService.addCoin(param);
        return new JsonResult("success");
    }
}
