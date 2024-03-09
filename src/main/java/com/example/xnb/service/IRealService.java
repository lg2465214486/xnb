package com.example.xnb.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.xnb.entity.Real;
import com.example.xnb.pojo.RealParam;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author .
 * @since 2023-11-20
 */
public interface IRealService extends IService<Real> {
    Real info(Integer userId);

    Object add(Integer userId, RealParam param);
}
