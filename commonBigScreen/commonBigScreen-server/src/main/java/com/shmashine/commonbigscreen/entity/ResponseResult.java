package com.shmashine.commonbigscreen.entity;

import java.util.Objects;

/**
 * 返回POJO类
 *
 * @author Liulifu
 * @version v1.0 - 2020/3/11 15:52
 */
public class ResponseResult {
    /**
     * 业务code
     */
    private String code;
    /**
     * 业务消息
     */
    private String message;
    /**
     * 返回数据对象
     */
    private Object info;

    public static final String CODE_OK = "0000";
    public static final String CODE_ERROR = "1111";
    public static final String CODE_VALID = "0001";

    public ResponseResult(String code, String msg) {
        this.code = code;
        this.message = msg;
    }

    public ResponseResult() {
    }

    public static ResponseResult resultValid(String msg) {
        ResponseResult responseResult = new ResponseResult();
        responseResult.setCode(CODE_VALID);
        responseResult.setMessage(msg);
        return responseResult;
    }


    /**
     * 普通返回
     *
     * @MethodName: Result
     * @Param: String code, String msg,Object info
     * @Return:
     * @Author: Liulifu
     * @Date: 17:37
     **/
    public ResponseResult(String code, String msg, Object info) {
        this.code = code;
        this.message = msg;
        this.info = info;
    }


    /**
     * 成功返回且无数据
     *
     * @MethodName: success
     * @Param:
     * @Return:
     * @Author: Liulifu
     * @Date: 17:37
     **/
    public static ResponseResult success() {
        return new ResponseResult(CODE_OK, "msg0000");
    }

    /**
     * 成功返回有数据
     *
     * @MethodName: success
     * @Param:
     * @Return:
     * @Author: Liulifu
     * @Date: 17:37
     **/
    public static ResponseResult successObj(Object object) {
        return new ResponseResult(CODE_OK, "msg0000", object);
    }


    /**
     * 错误返回且无数据
     *
     * @MethodName: error
     * @Param:
     * @Return:
     * @Author: Liulifu
     * @Date: 17:37
     **/
    public static ResponseResult error() {
        return new ResponseResult(CODE_ERROR, "msg1111");
    }

    public static ResponseResult error(String msg) {
        return new ResponseResult(CODE_ERROR, msg);
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getInfo() {
        return info;
    }

    public void setInfo(Object info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "Result{" + "code='" + code + '\'' + ", message='" + message + '\'' + ", info=" + info + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ResponseResult)) {
            return false;
        }
        ResponseResult responseResult = (ResponseResult) o;
        return Objects.equals(getCode(), responseResult.getCode())
                && Objects.equals(getMessage(), responseResult.getMessage())
                && Objects.equals(getInfo(), responseResult.getInfo());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCode(), getMessage(), getInfo());
    }
}
