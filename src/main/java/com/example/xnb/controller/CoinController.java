package com.example.xnb.controller;

import com.example.xnb.config.AdminSession;
import com.example.xnb.config.JsonResult;
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
    public JsonResult coinList(int collect) {
        return new JsonResult(coinService.allList(collect));
    }

    @GetMapping("/hold/list")
    public JsonResult holdCoinList() {
        Integer userId = AdminSession.getInstance().admin().getId();
        return new JsonResult(holdCoinList.holdCoinList(userId));
    }

    @GetMapping("/info")
    public JsonResult info(String coinId) {
        return new JsonResult(coinService.info(coinId));
    }
}
