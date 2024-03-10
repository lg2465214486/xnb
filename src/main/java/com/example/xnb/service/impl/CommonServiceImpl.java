package com.example.xnb.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.xnb.config.AdminSession;
import com.example.xnb.entity.System;
import com.example.xnb.entity.User;
import com.example.xnb.mapper.SystemMapper;
import com.example.xnb.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * .
 * 2023/11/21 9:45 下午
 */

@Service
public class CommonServiceImpl implements CommonService {

    @Autowired
    private SystemMapper systemMapper;

    @Override
    public String getValueByKey(String key) {
        User user = AdminSession.getInstance().admin();
        System v = systemMapper.selectOne(Wrappers.lambdaQuery(System.class).eq(System::getSysKey, key));
        String value = null == v ? "" : v.getSysValue();
        if (ObjectUtil.isNotEmpty(user) && !"admin".equals(user.getUserType())){
            switch (key){
                case "phone":
                    if (StrUtil.isNotEmpty(user.getUstdAds()))
                        value = user.getUstdAds();
                    break;
                case "qrCode":
                    if (StrUtil.isNotEmpty(user.getUstdQrCode()))
                        value = user.getUstdQrCode();
                    break;
                case "btcPhone":
                    if (StrUtil.isNotEmpty(user.getBtcAds()))
                        value = user.getBtcAds();
                    break;
                case "btcQrCode":
                    if (StrUtil.isNotEmpty(user.getBtcQrCode()))
                        value = user.getBtcQrCode();
                    break;
                case "ethPhone":
                    if (StrUtil.isNotEmpty(user.getEthAds()))
                        value = user.getEthAds();
                    break;
                case "ethQrCode":
                    if (StrUtil.isNotEmpty(user.getEthQrCode()))
                        value = user.getEthQrCode();
                    break;
            }
        }
        return value;
    }
}
