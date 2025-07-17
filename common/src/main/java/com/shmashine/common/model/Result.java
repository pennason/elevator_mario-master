package com.shmashine.common.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2019/3/29.
 */
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private String message;

    private T data;

    // 0表示成功，其他表示错误，由message露出
    private int code;

    public static Result success(Object data, String message) {
        Result result = new Result();
        result.setData(data);
        result.setMessage(message);
        result.setCode(0);
        return result;
    }

    public static Result success(String message) {
        Result result = new Result();
        result.setMessage(message);
        result.setCode(0);
        return result;
    }

    public static Result error() {
        Result result = new Result();
        result.setCode(1);
        result.setMessage("error");
        return result;
    }

    public static Result error(String message) {
        Result result = new Result();
        result.setCode(1);
        result.setMessage(message);
        return result;
    }

    public Result() {
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
