package com.example.xnb.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.xnb.entity.XNBTopUp;
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
public interface XNBTopUpMapper extends BaseMapper<XNBTopUp> {

    Page<XNBTopUp> selectPageByParam(Page<XNBTopUp> page, ListParam param);
}
