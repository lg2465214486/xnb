package com.example.xnb.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.xnb.entity.Withdraw;
import com.example.xnb.mapper.WithdrawMapper;
import com.example.xnb.pojo.ListParam;
import com.example.xnb.service.IWithdrawService;
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
public class WithdrawServiceImpl extends ServiceImpl<WithdrawMapper, Withdraw> implements IWithdrawService {

    @Autowired
    private WithdrawMapper withdrawMapper;

    @Override
    public Page<Withdraw> findWithdrawList(ListParam param) {
        Page<Withdraw> page = new Page<>();
        page.setCurrent(param.getPageNo());
        page.setSize(param.getPageSize());
        return withdrawMapper.selectPageByParam(page, param);
    }
}
