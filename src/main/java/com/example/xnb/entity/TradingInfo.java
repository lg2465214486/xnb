package com.example.xnb.entity;


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
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TradingInfo {

    private static final long serialVersionUID = 1L;

    private Integer userId;

    private String coinId;

    private BigDecimal price;

    private BigDecimal count;

    private BigDecimal singlePrice;

    private Integer status;

    private LocalDateTime time;
}
