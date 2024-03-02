package com.example.xnb.service;

import com.example.xnb.entity.XNBUnderway;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.xnb.pojo.MiningParam;
import com.example.xnb.pojo.dto.Earnings;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author .
 * @since 2023-11-20
 */
public interface IXNBUnderwayService extends IService<XNBUnderway> {

    String start(MiningParam param) throws Exception;

    String stop();

    Earnings findEarnings();
}
