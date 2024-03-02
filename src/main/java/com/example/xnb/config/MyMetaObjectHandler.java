package com.example.xnb.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Author：.
 * DATE：2022-12-2022/12/16 11:33
 * Description：<描述>
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    //插入时的填充策略
    @Override
    public void insertFill(MetaObject metaObject) {
        //是否开启了插入填充
//        this.openInsertFill();
        /* *
         * setFieldValByName方法有三个参数：
         * String fieldName, Object fieldVal, MetaObject metaObject
         * @param fieldName  Java实体类的属性名称
         * @param fieldVal   Java实体类的属性值
         * @param metaObject 数据源对象参数
         */
        //设置创建时间属性和值
        //3.3.0之前使用setFieldValByName方法
//        this.setFieldValByName("createTime",new Date(),metaObject);
        //3.3.0之后推荐使用strictInsertFill方法
        this.strictInsertFill(metaObject,"createdDate", LocalDateTime.class,LocalDateTime.now());
        //设置修改时间属性和值
        //3.3.0之前使用setFieldValByName方法
//        this.setFieldValByName("updateTime",new Date(),metaObject);
        //3.3.0之后推荐使用strictInsertFill方法
        this.strictInsertFill(metaObject,"updatedDate",LocalDateTime.class,LocalDateTime.now());
    }

    //更新时的填充策略
    @Override
    public void updateFill(MetaObject metaObject) {
        //设置修改时间属性和值
        //3.3.0之前使用setFieldValByName方法
//        this.setFieldValByName("updateTime",new Date(),metaObject);
        //3.3.0之后推荐使用strictUpdateFill方法
        this.strictInsertFill(metaObject,"updatedDate", LocalDateTime.class,LocalDateTime.now());
    }

    //修改严格填充策略
    //默认策略是：如果属性有值，则不覆盖；如果填充值为null，则不填充
    @Override
    public MetaObjectHandler strictFillStrategy(MetaObject metaObject, String fieldName, Supplier<?> fieldVal) {
        //每次填充，直接使用填充值
        //获取字段值，封装到Object对象中
        Object obj = fieldVal.get();
        //判断值是否为空
        if(Objects.nonNull(obj)) {
            metaObject.setValue(fieldName, obj);
        }
        return this;
    }
}
