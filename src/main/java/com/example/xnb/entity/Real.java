package com.example.xnb.entity;


import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("`real`")
public class Real {

    private static final long serialVersionUID = 1L;

    private Integer userId;

    private String firstName;
    private String lastName;
    private Integer sex; // 1 男  2 女
    private String address;
    private String phone;
    private String email;
}
