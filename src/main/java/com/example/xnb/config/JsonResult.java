package com.example.xnb.config;

import lombok.Data;

/**
 * @Author .
 * @Date 4/29/22
 */
@Data
public class JsonResult<T> {
    private Integer code;
    private String message;
    private T data;

    public JsonResult() {
        this.code = 200;
    }

    public JsonResult(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public JsonResult(T data) {
        this.code = 200;
        this.data = data;
    }

    public JsonResult(Integer code, T data) {
        this.code = code;
        this.data = data;
    }

    public JsonResult(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
}
