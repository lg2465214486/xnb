package com.example.xnb.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.xnb.entity.XNBWithdraw;
import com.example.xnb.mapper.XNBWithdrawMapper;
import com.example.xnb.pojo.ListParam;
import com.example.xnb.service.IXNBWithdrawService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author .
 * @since 2023-11-20
 */
@Service
public class XNBWithdrawServiceImpl extends ServiceImpl<XNBWithdrawMapper, XNBWithdraw> implements IXNBWithdrawService {

    @Autowired
    private XNBWithdrawMapper withdrawMapper;

    @Override
    public Page<XNBWithdraw> findWithdrawList(ListParam param) {
        Page<XNBWithdraw> page = new Page<>();
        page.setCurrent(param.getPageNo());
        page.setSize(param.getPageSize());
        return withdrawMapper.selectPageByParam(page, param);
    }
}
