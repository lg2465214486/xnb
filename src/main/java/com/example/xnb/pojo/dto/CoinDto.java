package com.example.xnb.pojo.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 *
 * 2024/03/09 4:20 下午
 */

@Data
public class CoinDto {
    private String id;

    private String name;

    private String image;

    private String info;

    private BigDecimal defaultPrice;

    private LocalDateTime issueDate;

    private Integer issueCount;

    private BigDecimal price;

    private Integer circulateCount;

    private String homeLink;

    private String whitePaper;

    private String blockchain;

    private BigDecimal increase24Hours;
    private BigDecimal high;
    private BigDecimal low;

    private Integer isCollect;
    private BigDecimal holdCount;
}
