package com.example.xnb.exception;

import com.example.xnb.config.JsonResult;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Log4j2
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public JsonResult exceptionHandler(Exception exception, HttpServletRequest request, HttpServletResponse response) {
        response.reset();
        response.setStatus(500);
        response.setContentType("application/json;charset=utf-8");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin") == null ? "" : request.getHeader("Origin"));
        JsonResult message = new JsonResult();
        message.setCode(500);
        if (exception instanceof GlobalException) {
            GlobalException globalException = (GlobalException) exception;
            message.setMessage(globalException.getMessage());
            response.setStatus(globalException.getCode());
            if (globalException.getE() != null) {
                log.error(globalException.getMessage(), globalException.getE());
            }
        } else if (exception instanceof EmptyResultDataAccessException) {
            message.setMessage("数据不存在 ");
        } else if (exception instanceof HttpMessageNotReadableException) {
            HttpMessageNotReadableException readableException = (HttpMessageNotReadableException) exception;
            String errorMsg = readableException.getMessage();
            String startStr = "Enum instance names:";
            errorMsg = errorMsg.substring(errorMsg.indexOf(startStr) + startStr.length());
//            errorMsg = errorMsg.substring(0, errorMsg.indexOf(";"));
            response.setStatus(400);
            message.setMessage("不支持的枚举类型, 仅支持 : " + errorMsg);
        } else {
            if (StringUtils.isEmpty(message.getMessage())) {
                message.setMessage(message.getMessage());
                log.error(exception.getMessage(), exception);
            } else {
                log.info(exception.getMessage(), exception);
            }
        }
        message.setMessage(exception.getMessage());
        return message;
    }
}