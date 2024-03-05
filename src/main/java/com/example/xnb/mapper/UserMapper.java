package com.example.xnb.mapper;

import com.example.xnb.entity.User;
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
public interface UserMapper extends BaseMapper<User> {

    @Select("select uuid from user;")
    List<String> findExistUuid();

    @Select("select * from user where uuid=#{uuid}")
    User selectByUuid(String uuid);
}
