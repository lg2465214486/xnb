package com.example.xnb.controller;


import com.example.xnb.config.JsonResult;
import com.example.xnb.pojo.MiningParam;
import com.example.xnb.service.IXNBUnderwayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mining")
public class MiningController {

    @Autowired
    private IXNBUnderwayService underwayService;

    /**
     * 总收益查询
     * @return
     */
    @GetMapping("/total")
    public JsonResult totalMining() {
        return new JsonResult("success");
    }

    /**
     * 收益查询
     * @return
     */
    @GetMapping("/earnings")
    public JsonResult findEarnings() {
        return new JsonResult(underwayService.findEarnings());
    }

    /**
     * 开始
     * @return
     */
    @PostMapping("/start")
    public JsonResult startMining(@RequestBody MiningParam param) throws Exception{
        return new JsonResult(underwayService.start(param));
    }

    /**
     * 停止
     * @return
     */
    @PostMapping("/stop")
    public JsonResult stopMining() {
        return new JsonResult(underwayService.stop());
    }

}
