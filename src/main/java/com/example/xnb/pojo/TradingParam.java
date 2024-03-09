package com.example.xnb.pojo;

import lombok.Data;

import java.math.BigDecimal;

/**
 *
 * 2024/03/09 3:40 下午
 */

@Data
public class TradingParam {

    Integer userId;
    String coinId;
    BigDecimal count;
    int status;
}
