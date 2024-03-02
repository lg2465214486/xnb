package com.example.xnb.pojo.dto;

import lombok.Data;

/**
 * .
 * 2023/11/21 11:24 下午
 */

@Data
public class UserInfo {
    /**
     * 登录私钥
     */
    String uuid;

    /**
     * 账户名
     */
    String userName;

    /**
     * 手机号码
     */
    String phone;

    /**
     * 邮箱
     */
    String userEmail;

    String ustd;

    String btc;

    String eth;

    String vipName;
}
