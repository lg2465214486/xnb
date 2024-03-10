package com.example.xnb.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.xnb.entity.Real;
import com.example.xnb.exception.GlobalException;
import com.example.xnb.mapper.RealMapper;
import com.example.xnb.mapper.UserMapper;
import com.example.xnb.pojo.RealParam;
import com.example.xnb.service.IRealService;
import com.sun.tools.doclint.Entity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.sun.tools.doclint.Entity.real;

/**
 *
 * 2024/03/05 9:24 下午
 */

@Service
public class RealServiceImpl extends ServiceImpl<RealMapper, Real> implements IRealService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public Real info(Integer userId) {
        return this.getOne(new LambdaQueryWrapper<Real>().eq(Real::getUserId, userId));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Object add(Integer userId, RealParam param) {
        List<Real> list = this.list(new LambdaQueryWrapper<Real>().eq(Real::getUserId, userId));
        if (list.size() > 0) {
            throw new GlobalException(500, "已实名");
        }
        Real real = new Real();
        BeanUtils.copyProperties(param, real);
        real.setUserId(userId);
        this.save(real);
        return "success";
    }
}
