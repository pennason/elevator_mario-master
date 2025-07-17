package com.shmashine.haierCamera.entity;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author jiangheng
 * @version 1.0
 * @date 2021/10/21 14:00
 */
public class HaierCameraResponseResult implements Serializable {

    private static final Long serialVersionUID = 464531456464L;

    /**
     * 是否请求成功，0-成功，1-失败
     */
    private Integer resultCode;

    /**
     * 错误编码信息，0-成功，失败为其他(例如500等)
     */
    private Integer errorCode;

    /**
     * 请求成功/失败信息
     */
    private String msg;

    public HaierCameraResponseResult() {
    }

    public HaierCameraResponseResult(Integer resultCode, Integer errorCode, String msg) {
        this.resultCode = resultCode;
        this.errorCode = errorCode;
        this.msg = msg;
    }

    public static HaierCameraResponseResult success() {
        return new HaierCameraResponseResult(0, 0, "success");
    }

    public static HaierCameraResponseResult successMessage(String msg) {
        return new HaierCameraResponseResult(0, 0, msg);
    }

    public static HaierCameraResponseResult error(String msg) {
        return new HaierCameraResponseResult(0, 1, msg);
    }


    public Integer getResultCode() {
        return resultCode;
    }

    public void setResultCode(Integer resultCode) {
        this.resultCode = resultCode;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "HaierCameraResponseResult{" +
                "resultCode=" + resultCode +
                ", errorCode=" + errorCode +
                ", msg='" + msg + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HaierCameraResponseResult that = (HaierCameraResponseResult) o;
        return Objects.equals(resultCode, that.resultCode) &&
                Objects.equals(errorCode, that.errorCode) &&
                Objects.equals(msg, that.msg);
    }

    @Override
    public int hashCode() {
        return Objects.hash(resultCode, errorCode, msg);
    }
}
