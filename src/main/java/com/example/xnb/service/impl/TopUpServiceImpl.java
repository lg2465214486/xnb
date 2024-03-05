package com.example.xnb.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.xnb.entity.TopUp;
import com.example.xnb.mapper.TopUpMapper;
import com.example.xnb.pojo.ListParam;
import com.example.xnb.service.ITopUpService;
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
public class TopUpServiceImpl extends ServiceImpl<TopUpMapper, TopUp> implements ITopUpService {

    @Autowired
    private TopUpMapper topUpMapper;
    @Override
    public Page<TopUp> findTopUpList(ListParam param) {
        Page<TopUp> page = new Page<>();
        page.setCurrent(param.getPageNo());
        page.setSize(param.getPageSize());
        return topUpMapper.selectPageByParam(page, param);
    }
}
