package com.shmashine.hikYunMou.exception;


import com.shmashine.hikYunMou.responseAdvice.ReturnCode;

import lombok.Getter;


@Getter
public class MyException extends RuntimeException {
    /**
     * 异常状态码信息
     */
    private int status;

    public MyException() {
        super(ReturnCode.RC500.getMessage());
        this.status = ReturnCode.RC500.getCode();
    }

    public MyException(int status) {
        this.status = status;
    }

    public MyException(String message) {
        super(message);
        this.status = ReturnCode.RC500.getCode();
    }

    public MyException(int status, String message) {
        super(message);
        this.status = status;
    }

    /**
     * 三个参数，分别为状态码，消息内容，异常的起源
     */
    public MyException(int status, String message, Throwable cause) {
        super(message, cause);
        this.status = status;
    }

    public MyException(int status, Throwable cause) {
        super(cause);
        this.status = status;
    }
}