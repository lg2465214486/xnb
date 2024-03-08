package com.example.xnb.controller;

import com.example.xnb.config.JsonResult;
import com.example.xnb.pojo.AddCoinParam;
import com.example.xnb.service.ICoinService;
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

    /**
     * 添加coin
     * @return
     */
    @PostMapping("/add")
    public JsonResult add(@RequestBody AddCoinParam param) {
        coinService.addCoin(param);
        return new JsonResult("success");
    }

    /**
     * 删除coin
     * @return
     */
    @PostMapping("/del")
    public JsonResult del(String coinId) {
        coinService.delCoin(coinId);
        return new JsonResult("success");
    }

    /**
     * 设置涨幅
     * @return
     */
    @PostMapping("/setIncrease")
    public JsonResult setIncrease(String coinId, int increase) {
        coinService.setIncrease(coinId, increase);
        return new JsonResult("success");
    }

    @GetMapping("/list")
    public Object userList(int collect) {
        return coinService.allList(collect);
    }
}
