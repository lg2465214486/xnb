package com.example.xnb.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.xnb.entity.TopUp;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.xnb.pojo.ListParam;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author .
 * @since 2023-11-20
 */
public interface TopUpMapper extends BaseMapper<TopUp> {

    Page<TopUp> selectPageByParam(Page<TopUp> page, ListParam param);
}
