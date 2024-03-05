package com.example.xnb.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
public class Underway implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer userId;

    /**
     * 质押数量
     */
    private BigDecimal moneyQuantity;

    /**
     * 收益
     */
    private BigDecimal earnings;

    /**
     * 1:进行中，2:已暂停
     */
    private Integer status;

    /**
     * 开始时间
     */
    private LocalDateTime startDate;

    /**
     * 结束时间
     */
    private LocalDateTime endDate;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;


}
