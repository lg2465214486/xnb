package com.example.xnb.service;

import com.example.xnb.entity.XNBSystem;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.xnb.pojo.ExamineParam;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author .
 * @since 2023-11-20
 */
public interface IXNBSystemService extends IService<XNBSystem> {

    String editKeyValue(String key, String value);

    String topUpExamine(ExamineParam param);

    String withdrawExamine(ExamineParam param);
}
