package com.example.xnb.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Author：.
 * DATE：2022-12-2022/12/14 13:49
 * Description：<描述>
 */
@Configuration
public class PreloadBean {

    /**
     * 开启mybatis-plus插件
     * @return
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        //开启mybatis-plus乐观锁
        //interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        //开启mybatis-plus分页插件
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

}
