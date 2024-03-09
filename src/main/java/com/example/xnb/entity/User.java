package com.example.xnb.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
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
@TableName("`user`")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 登录私钥
     */
    private String uuid;

    /**
     * 密码
     */
    private String pwd;

    /**
     * 账户名
     */
    private String userName;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 邮箱
     */
    private String userEmail;

    private BigDecimal ustd;

    private BigDecimal btc;

    private BigDecimal eth;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)		// 反序列化
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createdDate;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)		// 反序列化
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime updatedDate;

    /**
     * 是否禁用  0：否  1：是
     */
    private Boolean isStop;

    /**
     * 是否实名  0：否  1：是
     */
    private Integer isReal;

    private String token;

    private String userType;

    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Integer vipGrade;

    private String ustdAds;
    private String ustdQrCode;
    private String btcAds;
    private String btcQrCode;
    private String ethAds;
    private String ethQrCode;

    @TableField(exist = false)
    private String vipName;

    @TableField(exist = false)
    private Real realInfo;
}
