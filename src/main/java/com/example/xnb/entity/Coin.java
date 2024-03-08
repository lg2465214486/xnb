package com.example.xnb.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
public class Coin implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.NONE)
    private String id;

    private String name;

    private String image;

    private String info;

    private BigDecimal defaultPrice;

    private LocalDateTime issueDate;

    private Integer issueCount;

    private BigDecimal price;

    private Integer increase;

    private Integer isDel;

    private Integer circulateCount;

    private String homeLink;

    private String whitePaper;

    @TableField(exist = false)
    private BigDecimal startPrice;

}
