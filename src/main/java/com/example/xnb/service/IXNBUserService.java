package com.example.xnb.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.xnb.config.JsonResult;
import com.example.xnb.entity.XNBUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.xnb.pojo.*;
import com.example.xnb.pojo.dto.DealDetail;
import com.example.xnb.pojo.dto.UserInfo;

import java.util.List;


/**
 * <p>
 *  服务类
 * </p>
 *
 * @author .
 * @since 2023-11-20
 */
public interface IXNBUserService extends IService<XNBUser> {

    JsonResult userLogin(LoginParam login);

    JsonResult adminUserLogin(LoginParam login);

    Page<XNBUser> findUserList(ListParam param);

    JsonResult addUser(UserParam param);

    List<DealDetail> getTransactionRecordById(Integer id);

    UserInfo getUserInfo();

    JsonResult editUser(UserParam param);

    String topUp(MoneyOptionParam param);

    String withdraw(MoneyOptionParam param) throws Exception;

    String conversion(ConversionParam param) throws Exception;

    String stopOrOpenUser(String uuid, int isStop);
}
