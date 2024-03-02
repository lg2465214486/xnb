package com.example.xnb.pojo.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * .
 * 2023/11/21 10:59 下午
 */

@Data
public class DealDetail implements Comparable<DealDetail>{
    String time;
    String type; // 1:充值 0:提现
    String amount;
    String trc20;
    String status;

    LocalDateTime localDateTime;

    @Override
    public int compareTo(DealDetail d) {
        return d.getLocalDateTime().compareTo(this.localDateTime);
    }
}
