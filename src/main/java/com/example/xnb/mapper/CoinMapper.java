package com.example.xnb.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.xnb.entity.Coin;
import com.example.xnb.pojo.dto.CoinDto;
import com.example.xnb.pojo.dto.CoinListDto;
import org.apache.ibatis.annotations.Param;

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

    List<CoinListDto> selectAllList(@Param("ids") List<String> ids, @Param("now") String now, @Param("yestoday") String yestoday);

    CoinDto selectInfo(@Param("id") String id, @Param("now") String now, @Param("today") String today, @Param("yestoday") String yestoday);
}
