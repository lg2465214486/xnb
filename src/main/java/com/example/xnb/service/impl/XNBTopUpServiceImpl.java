package com.example.xnb.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.xnb.entity.XNBTopUp;
import com.example.xnb.mapper.XNBTopUpMapper;
import com.example.xnb.pojo.ListParam;
import com.example.xnb.service.IXNBTopUpService;
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
public class XNBTopUpServiceImpl extends ServiceImpl<XNBTopUpMapper, XNBTopUp> implements IXNBTopUpService {

    @Autowired
    private XNBTopUpMapper topUpMapper;
    @Override
    public Page<XNBTopUp> findTopUpList(ListParam param) {
        Page<XNBTopUp> page = new Page<>();
        page.setCurrent(param.getPageNo());
        page.setSize(param.getPageSize());
        return topUpMapper.selectPageByParam(page, param);
    }
}
