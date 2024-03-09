package com.example.xnb.pojo.dto;


import com.example.xnb.entity.Coin;
import com.example.xnb.entity.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 *
 * 2024/03/09 3:32 下午
 */

@Data
public class TradingSelectDto {

    private Integer userId;

    private String coinId;

    private BigDecimal price;

    private BigDecimal count;

    private BigDecimal singlePrice;

    private Integer status;

    private LocalDateTime time;

    private User user;

    private Coin coin;
}
