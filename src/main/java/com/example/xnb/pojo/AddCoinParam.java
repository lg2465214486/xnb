package com.example.xnb.pojo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 *
 * 2024/03/05 9:22 下午
 */

@Data
public class AddCoinParam {
     String name;

     String image;

     String info;

     BigDecimal startPrice;

     LocalDateTime issueDate;

     Integer issueCount;

     BigDecimal price;

     Integer circulateCount;

     String homeLink;

     String whitePaper;
}
