package com.example.xnb.pojo.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 *
 * 2024/03/09 4:05 下午
 */

@Data
public class CoinListDto {
    private String id;

    private String name;

    private String image;

    private BigDecimal price;

    private BigDecimal increase24Hours;

}
