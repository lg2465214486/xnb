package com.example.xnb.config;

/**
 * Author：.
 * DATE：2022-06-2022/6/20 17:44
 * Description：<描述>
 */
public class JsonResultUtil {

    public static JsonResult success(Object result){
        JsonResult jsonResult = new JsonResult<Object>();
        jsonResult.setCode(200);
        jsonResult.setMessage("success");
        jsonResult.setData(result);
        return jsonResult;
    }

    public static JsonResult fail(String message,Object result){
        JsonResult jsonResult = new JsonResult<Object>();
        jsonResult.setCode(510);
        jsonResult.setMessage(message);
        jsonResult.setData(result);
        return jsonResult;
    }

    public static JsonResult fail(Integer code,String message){
        JsonResult jsonResult = new JsonResult<Object>();
        jsonResult.setCode(code);
        jsonResult.setMessage(message);
        jsonResult.setData(null);
        return jsonResult;
    }

    public static JsonResult success(){
        JsonResult jsonResult = new JsonResult<Object>();
        jsonResult.setCode(200);
        jsonResult.setMessage("success");
        return jsonResult;
    }

}
