package com.example.xnb.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.xnb.entity.Coin;
import com.example.xnb.pojo.dto.CoinDto;
import com.example.xnb.pojo.dto.CoinListDto;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
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

    List<CoinListDto> selectAllList(@Param("ids") List<String> ids, @Param("now") String now, @Param("yesterday") String yesterday);

    CoinDto selectInfo(@Param("id") String id, @Param("today") String today, @Param("yesterday") String yesterday);

    @Update("update coin set increase = null where id = #{id}")
    void setIncreaseNullById(@Param("id") String coinId);
}
