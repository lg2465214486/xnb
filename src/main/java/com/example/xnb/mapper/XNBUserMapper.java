package com.example.xnb.mapper;

import com.example.xnb.entity.XNBUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;


/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author .
 * @since 2023-11-20
 */
public interface XNBUserMapper extends BaseMapper<XNBUser> {

    @Select("select uuid from xnb_user;")
    List<String> findExistUuid();

    @Select("select * from xnb_user where uuid=#{uuid}")
    XNBUser selectByUuid(String uuid);
}
