package com.example.xnb.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class GlobalException extends RuntimeException {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private int code = 500;
    private String message;
    private Throwable e;

    public GlobalException(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public GlobalException(int code, String message, Throwable e) {
        this.code = code;
        this.message = message;
        this.e = e;
    }



}