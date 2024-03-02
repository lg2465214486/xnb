package com.example.xnb.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.xnb.config.AdminSession;
import com.example.xnb.entity.XNBSystem;
import com.example.xnb.entity.XNBUser;
import com.example.xnb.mapper.XNBSystemMapper;
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
    private XNBSystemMapper systemMapper;

    @Override
    public String getValueByKey(String key) {
        XNBUser xnbUser = AdminSession.getInstance().admin();
        XNBSystem v = systemMapper.selectOne(Wrappers.lambdaQuery(XNBSystem.class).eq(XNBSystem::getSysKey, key));
        String value = null == v ? "" : v.getSysValue();
        if (ObjectUtil.isNotEmpty(xnbUser) && !"admin".equals(xnbUser.getUserType())){
            switch (key){
                case "phone":
                    if (StrUtil.isNotEmpty(xnbUser.getBtcAds()))
                        value = xnbUser.getUstdAds();
                    break;
                case "qrCode":
                    if (StrUtil.isNotEmpty(xnbUser.getBtcAds()))
                        value = xnbUser.getUstdQrCode();
                    break;
                case "btcPhone":
                    if (StrUtil.isNotEmpty(xnbUser.getBtcAds()))
                        value = xnbUser.getBtcAds();
                    break;
                case "btcQrCode":
                    if (StrUtil.isNotEmpty(xnbUser.getBtcAds()))
                        value = xnbUser.getBtcQrCode();
                    break;
                case "ethPhone":
                    if (StrUtil.isNotEmpty(xnbUser.getBtcAds()))
                        value = xnbUser.getEthAds();
                    break;
                case "ethQrCode":
                    if (StrUtil.isNotEmpty(xnbUser.getBtcAds()))
                        value = xnbUser.getEthQrCode();
                    break;
            }
        }
        return value;
    }
}
