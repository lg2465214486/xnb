package com.example.xnb.entity;

import cn.hutool.core.date.LocalDateTimeUtil;
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

    private String time;

    private Integer week;

    public LocalDateTime getTime() {
        return LocalDateTime.parse(time.replace(" ", "T"));
    }

    public void setTime(LocalDateTime time) {
        this.time = LocalDateTimeUtil.format(time, "yyyy-MM-dd HH:mm:ss");
    }

    public void setTime(String time) {
        this.time = time;
    }
}
