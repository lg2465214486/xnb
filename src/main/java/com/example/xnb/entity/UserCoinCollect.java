package com.example.xnb.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

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
public class UserCoinCollect implements Serializable {

    private static final long serialVersionUID = 1L;

    private String coinId;

    private Integer userId;


}
