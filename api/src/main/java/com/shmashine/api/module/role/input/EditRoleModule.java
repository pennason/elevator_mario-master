package com.shmashine.api.module.role.input;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @PackgeName: com.shmashine.api.module.role.input
 * @ClassName: EditRoleModule
 * @Date: 2020/6/99:50
 * @Author: LiuLiFu
 * @Description: 编辑角色参数类
 */
public class EditRoleModule {

    @NotNull(message = "请输入角色编号")
    /**角色编号*/
    private String vRoleId;

    @NotNull(message = "请输入角色名称")
    @Length(max = 100, min = 1, message = "角色名称不能超过100个字，且不能小于1个字")
    /**角色名称*/
    private String vRoleName;

    /**
     * 角色备注
     */
    private String vRemark;

    /**
     * 拥有的菜单
     */
    @NotNull(message = "权限不能为空")
    @Size(min = 1, message = "最少请选择一个菜单权限")
    private List<String> menu;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    /**修改时间*/
    private Date dtModifytime;
    /**
     * 修改人
     */
    private String vModifyid;
    /**
     * 登陆是否验证手机
     */
    private Boolean bLoginVerifyPhone;

    public String getvRoleId() {
        return vRoleId;
    }

    public void setvRoleId(String vRoleId) {
        this.vRoleId = vRoleId;
    }

    public String getvRoleName() {
        return vRoleName;
    }

    public void setvRoleName(String vRoleName) {
        this.vRoleName = vRoleName;
    }

    public String getvRemark() {
        return vRemark;
    }

    public void setvRemark(String vRemark) {
        this.vRemark = vRemark;
    }

    public List<String> getMenu() {
        return menu;
    }

    public void setMenu(List<String> menu) {
        this.menu = menu;
    }

    public Date getDtModifytime() {
        return dtModifytime;
    }

    public void setDtModifytime(Date dtModifytime) {
        this.dtModifytime = dtModifytime;
    }

    public String getvModifyid() {
        return vModifyid;
    }

    public void setvModifyid(String vModifyid) {
        this.vModifyid = vModifyid;
    }

    public Boolean getbLoginVerifyPhone() {
        return bLoginVerifyPhone;
    }

    public void setbLoginVerifyPhone(Boolean bLoginVerifyPhone) {
        this.bLoginVerifyPhone = bLoginVerifyPhone;
    }
}
