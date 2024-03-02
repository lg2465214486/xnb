package com.example.xnb.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.xnb.entity.XNBWithdraw;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.xnb.pojo.ListParam;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author .
 * @since 2023-11-20
 */
public interface IXNBWithdrawService extends IService<XNBWithdraw> {

    Page<XNBWithdraw> findWithdrawList(ListParam param);
}
