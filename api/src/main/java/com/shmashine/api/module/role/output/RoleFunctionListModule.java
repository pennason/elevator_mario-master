package com.shmashine.api.module.role.output;

import java.util.List;

/**
 * @PackgeName: com.shmashine.api.entity.permission
 * @ClassName: RoleFunctionListModule
 * @Date: 2020/6/915:08
 * @Author: LiuLiFu
 * @Description: 角色接口返回实体
 */
public class RoleFunctionListModule {
    private String value;
    private String title;
    private String parentId;
    private String type;
    private String flag;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private List<RoleFunctionListModule> children;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public List<RoleFunctionListModule> getChildren() {
        return children;
    }

    public void setChildren(List<RoleFunctionListModule> children) {
        this.children = children;
    }
}
