package com.shmashine.camera.model.base;


import java.util.Map;
import java.util.Objects;

import com.shmashine.camera.message.BusinessMessageResouseConfig;

/**
 * @PackageName org.sulotion.entity
 * @ClassName Result
 * @Date 2020/3/11 15:52
 * @Author Liulifu
 * Version v1.0
 * @description 返回POJO类
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

    public static final String CODE_BINDING_ERROR = "1112";
    public static final String CODE_INVALID_ERROR = "1113";
    public static final String CODE_VALID = "0001";

    // 录制视频
    public static final String CODE_VIDEO_ERROR = "2001";
    // 截图
    public static final String CODE_IMAGE_ERROR = "2001";


    public ResponseResult(String code, String msg) {
        this.code = code;
        Map<String, String> msgPropertiesMap = BusinessMessageResouseConfig.msgPropertiesMap;
        this.message = msgPropertiesMap.getOrDefault(msg, msg);
    }

    public void ResponseResult(String code, String msg) {
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
     * @MethodName: Result
     * @Description: 普通返回
     * @Param: String code, String msg,Object info
     * @Return:
     * @Author: Liulifu
     * @Date: 17:37
     **/
    public ResponseResult(String code, String msg, Object info) {
        this.code = code;
        Map<String, String> msgPropertiesMap = BusinessMessageResouseConfig.msgPropertiesMap;
        this.message = msgPropertiesMap.get(msg);
        this.info = info;
    }

    /**
     * @MethodName: Result
     * @Description: 占位符替换
     * @Param: String code, String msg, Object info, List<String> params
     * @Return:
     * @Author: Liulifu
     * @Date: 17:36
     **/
    public ResponseResult(String code, String msg, Object info, String[] params) {
        this.code = code;
        Map<String, String> msgPropertiesMap = BusinessMessageResouseConfig.msgPropertiesMap;
        String temp = msgPropertiesMap.get(msg);
        var msgList = temp.split("%");
        //String[] msgList = StringUtils.splitString(temp, "%");
        if (params.length + 1 == msgList.length) {
            int k = 0;
            String msgInfo = msgList[0];
            for (int i = 1; i < msgList.length; i++) {
                k = i - 1;
                msgInfo += params[k] + msgList[i];
            }
            this.message = msgInfo;
        }
        this.info = info;
    }

    public ResponseResult(String code, String msg, String[] params) {
        this.code = code;
        Map<String, String> msgPropertiesMap = BusinessMessageResouseConfig.msgPropertiesMap;
        String temp = msgPropertiesMap.get(msg);
        var msgList = temp.split("%");
        //String[] msgList = StringUtils.splitString(temp, "%");
        if (params.length + 1 == msgList.length) {
            int k = 0;
            String msgInfo = msgList[0];
            for (int i = 1; i < msgList.length; i++) {
                k = i - 1;
                msgInfo += params[k] + msgList[i];
            }
            this.message = msgInfo;
        }
    }


    /**
     * @MethodName: success
     * @Description: 成功返回且无数据
     * @Param:
     * @Return:
     * @Author: Liulifu
     * @Date: 17:37
     **/
    public static ResponseResult success() {
        return new ResponseResult(CODE_OK, "msg0000");
    }

    /**
     * @MethodName: success
     * @Description: 成功返回有数据
     * @Param:
     * @Return:
     * @Author: Liulifu
     * @Date: 17:37
     **/
    public static ResponseResult successObj(Object object) {
        return new ResponseResult(CODE_OK, "msg0000", object);
    }


    /**
     * @MethodName: error
     * @Description: 错误返回且无数据
     * @Param:
     * @Return:
     * @Author: Liulifu
     * @Date: 17:37
     **/
    public static ResponseResult error() {
        return new ResponseResult(CODE_ERROR, "msg1111");
    }

    public static ResponseResult error(String msg) {
        return error(CODE_ERROR, msg);
    }

    public static ResponseResult error(String code, String msg) {
        Map<String, String> msgPropertiesMap = BusinessMessageResouseConfig.msgPropertiesMap;
        return new ResponseResult(code, msgPropertiesMap.getOrDefault(msg, msg));
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
        return "Result{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", info=" + info +
                '}';
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
        return Objects.equals(getCode(), responseResult.getCode()) &&
                Objects.equals(getMessage(), responseResult.getMessage()) &&
                Objects.equals(getInfo(), responseResult.getInfo());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCode(), getMessage(), getInfo());
    }
}
