package com.example.xnb;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Author：.
 * DATE：2023-11-2023/11/20 21:59
 * Description：<描述>
 */
@EnableScheduling
@SpringBootApplication
@MapperScan({"com.example.xnb.mapper"})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
