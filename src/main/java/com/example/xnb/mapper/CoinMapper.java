package com.example.xnb.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.xnb.entity.Coin;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author .
 * @since 2023-11-20
 */
public interface CoinMapper extends BaseMapper<Coin> {

    List<Coin> selectNeedGenerateCoin(@Param("now") String now);
}
