package com.shmashine.api.module.login.output;

import java.io.Serializable;
import java.util.List;

/**
 * 权限信息封装类
 */
public class ResponsePermission implements Serializable {
    private String parentId;
    private String functionId;
    private String type;
    private String functionName;
    private String functionIcon;
    private String path;
    private String status;
    private Integer orderNumber;

    private List<ResponsePermission> children;

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }


    public String getFunctionId() {
        return functionId;
    }

    public void setFunctionId(String functionId) {
        this.functionId = functionId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public String getFunctionIcon() {
        return functionIcon;
    }

    public void setFunctionIcon(String functionIcon) {
        this.functionIcon = functionIcon;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ResponsePermission> getChildren() {
        return children;
    }

    public void setChildren(List<ResponsePermission> children) {
        this.children = children;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }
}
