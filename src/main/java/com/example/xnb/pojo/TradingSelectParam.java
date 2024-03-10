package com.example.xnb.pojo;

import lombok.Data;

import java.math.BigDecimal;

/**
 *
 * 2024/03/09 3:40 下午
 */

@Data
public class TradingSelectParam {
    String keywords;
    long pageNo;
    long pageSize;
    String coinId;
    BigDecimal count;
    BigDecimal maxPrice;
    BigDecimal minPrice;
    Integer status;
}
