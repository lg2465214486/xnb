package com.example.xnb.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.xnb.entity.UserCoinCollect;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author .
 * @since 2023-11-20
 */
public interface UserCoinCollectMapper extends BaseMapper<UserCoinCollect> {

    @Delete("delete from user_coin_collect where user_id=#{userId} and coin_id=#{coinId}")
    void removeCollect(@Param("userId") Integer userId, @Param("coinId")String coinId);
}
