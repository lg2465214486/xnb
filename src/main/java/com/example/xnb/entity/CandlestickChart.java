package com.example.xnb.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author .
 * @since 2023-11-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CandlestickChart implements Serializable {

    private static final long serialVersionUID = 1L;

    private String coinId;

    private BigDecimal price;

    private BigDecimal minPrice;

    private BigDecimal maxPrice;

    private LocalDateTime time;

    private Integer week;


}
