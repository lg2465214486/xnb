package com.example.xnb.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
public class TopUp implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer userId;

    /**
     * 金额
     */
    private BigDecimal sales;

    /**
     * 审核状态 1：待审核，2：通过，3：不通过
     */
    private Integer status;

    private LocalDateTime updatedDate;

    private LocalDateTime createdDate;

    private String bz;

    @TableField(exist = false)
    private String uuid;
}
