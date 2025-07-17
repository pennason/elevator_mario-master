package com.shmashine.common.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * (TblThridPartyRuijinCheck)实体类
 *
 * @author makejava
 * @since 2020-07-24 14:44:44
 */
public class TblThridPartyRuijinCheck implements Serializable {
    @Serial
    private static final long serialVersionUID = -54612888918530407L;
    /**
     * 注册代码
     */
    private String registerNumber;
    /**
     * 检验单位名
     */
    private String inspectOrgName;
    /**
     * 检验单位代码
     */
    private String inspectOrgCode;
    /**
     * 检验单位法人代表人
     */
    private String inspectOrgLegalName;
    /**
     * 检验单位法人代表人身份证
     */
    private String inspectOrgLegalId;
    /**
     * 检验类别
     */
    private String inspectType;
    /**
     * 施工类别
     */
    private String constructionCompanyName;
    /**
     * 施工单位名称
     */
    private String constructionType;
    /**
     * 施工单位许可证明文件编号
     */
    private String certNumber;
    /**
     * 检验员名称
     */
    private String inspector;
    /**
     * 检验员身份证
     */
    private String inspectorId;
    /**
     * 主要问题
     */
    private String problem;
    /**
     * 检验结论
     */
    private String result;
    /**
     * 报告书编号
     */
    private String reportNumber;
    /**
     * 检验日期
     */
    private Date inspectDate;
    /**
     * 下次检验日期
     */
    private Date nextInspectDate;
    /**
     * 检验规范版本号
     */
    private String standardVersion;
    /**
     * 检验机构核准证编号
     */
    private String inspectOrgApprNumber;
    /**
     * 检验不合格项
     */
    private String unqualifiedItems;
    /**
     * 编制人
     */
    private String preparer;
    /**
     * 编制日期
     */
    private Object prepareDate;
    /**
     * 审核人
     */
    private String reviewer;
    /**
     * 审核日期
     */
    private Object reviewDate;
    /**
     * 批注人
     */
    private String approver;
    /**
     * 批准日期
     */
    private Object approvedDate;
    /**
     * 主要检验仪器设备
     */
    private String inspectDevices;
    /**
     * 备注
     */
    private String comment;
    /**
     * 检验记录编码
     */
    private String jyid;

    /**
     * 是否超期 0正常，1：超期
     */
    private Integer overdue;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date dtCreateTime;
    /**
     * 修改时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date dtModifyTime;
    /**
     * 创建记录用户
     */
    private String vCreateUserId;
    /**
     * 修改记录用户
     */
    private String vModifyUserId;

    @JsonProperty("registerNumber")
    public String getRegisterNumber() {
        return registerNumber;
    }

    public void setRegisterNumber(String registerNumber) {
        this.registerNumber = registerNumber;
    }

    @JsonProperty("inspectOrgName")
    public String getInspectOrgName() {
        return inspectOrgName;
    }

    public void setInspectOrgName(String inspectOrgName) {
        this.inspectOrgName = inspectOrgName;
    }

    @JsonProperty("inspectOrgCode")
    public String getInspectOrgCode() {
        return inspectOrgCode;
    }

    public void setInspectOrgCode(String inspectOrgCode) {
        this.inspectOrgCode = inspectOrgCode;
    }

    @JsonProperty("inspectOrgLegalName")
    public String getInspectOrgLegalName() {
        return inspectOrgLegalName;
    }

    public void setInspectOrgLegalName(String inspectOrgLegalName) {
        this.inspectOrgLegalName = inspectOrgLegalName;
    }

    @JsonProperty("inspectOrgLegalId")
    public String getInspectOrgLegalId() {
        return inspectOrgLegalId;
    }

    public void setInspectOrgLegalId(String inspectOrgLegalId) {
        this.inspectOrgLegalId = inspectOrgLegalId;
    }

    @JsonProperty("inspectType")
    public String getInspectType() {
        return inspectType;
    }

    public void setInspectType(String inspectType) {
        this.inspectType = inspectType;
    }

    @JsonProperty("constructionCompanyName")
    public String getConstructionCompanyName() {
        return constructionCompanyName;
    }

    public void setConstructionCompanyName(String constructionCompanyName) {
        this.constructionCompanyName = constructionCompanyName;
    }

    @JsonProperty("constructionType")
    public String getConstructionType() {
        return constructionType;
    }

    public void setConstructionType(String constructionType) {
        this.constructionType = constructionType;
    }

    @JsonProperty("certNumber")
    public String getCertNumber() {
        return certNumber;
    }

    public void setCertNumber(String certNumber) {
        this.certNumber = certNumber;
    }

    @JsonProperty("inspector")
    public String getInspector() {
        return inspector;
    }

    public void setInspector(String inspector) {
        this.inspector = inspector;
    }

    @JsonProperty("inspectorId")
    public String getInspectorId() {
        return inspectorId;
    }

    public void setInspectorId(String inspectorId) {
        this.inspectorId = inspectorId;
    }

    @JsonProperty("problem")
    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    @JsonProperty("result")
    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @JsonProperty("reportNumber")
    public String getReportNumber() {
        return reportNumber;
    }

    public void setReportNumber(String reportNumber) {
        this.reportNumber = reportNumber;
    }

    @JsonProperty("inspectDate")
    public Date getInspectDate() {
        return inspectDate;
    }

    public void setInspectDate(Date inspectDate) {
        this.inspectDate = inspectDate;
    }

    @JsonProperty("nextInspectDate")
    public Date getNextInspectDate() {
        return nextInspectDate;
    }

    public void setNextInspectDate(Date nextInspectDate) {
        this.nextInspectDate = nextInspectDate;
    }

    @JsonProperty("standardVersion")
    public String getStandardVersion() {
        return standardVersion;
    }

    public void setStandardVersion(String standardVersion) {
        this.standardVersion = standardVersion;
    }

    @JsonProperty("inspectOrgApprNumber")
    public String getInspectOrgApprNumber() {
        return inspectOrgApprNumber;
    }

    public void setInspectOrgApprNumber(String inspectOrgApprNumber) {
        this.inspectOrgApprNumber = inspectOrgApprNumber;
    }

    @JsonProperty("unqualifiedItems")
    public String getUnqualifiedItems() {
        return unqualifiedItems;
    }

    public void setUnqualifiedItems(String unqualifiedItems) {
        this.unqualifiedItems = unqualifiedItems;
    }

    @JsonProperty("preparer")
    public String getPreparer() {
        return preparer;
    }

    public void setPreparer(String preparer) {
        this.preparer = preparer;
    }

    @JsonProperty("prepareDate")
    public Object getPrepareDate() {
        return prepareDate;
    }

    public void setPrepareDate(Object prepareDate) {
        this.prepareDate = prepareDate;
    }

    @JsonProperty("reviewer")
    public String getReviewer() {
        return reviewer;
    }

    public void setReviewer(String reviewer) {
        this.reviewer = reviewer;
    }

    @JsonProperty("reviewDate")
    public Object getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(Object reviewDate) {
        this.reviewDate = reviewDate;
    }

    @JsonProperty("approver")
    public String getApprover() {
        return approver;
    }

    public void setApprover(String approver) {
        this.approver = approver;
    }

    @JsonProperty("approvedDate")
    public Object getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(Object approvedDate) {
        this.approvedDate = approvedDate;
    }

    @JsonProperty("inspectDevices")
    public String getInspectDevices() {
        return inspectDevices;
    }

    public void setInspectDevices(String inspectDevices) {
        this.inspectDevices = inspectDevices;
    }

    @JsonProperty("comment")
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @JsonProperty("jyid")
    public String getJyid() {
        return jyid;
    }

    public Integer getOverdue() {
        return overdue;
    }

    public void setOverdue(Integer overdue) {
        this.overdue = overdue;
    }

    public void setJyid(String jyid) {
        this.jyid = jyid;
    }

    @JsonProperty("dtCreateTime")
    public Date getDtCreateTime() {
        return dtCreateTime;
    }

    public void setDtCreateTime(Date dtCreateTime) {
        this.dtCreateTime = dtCreateTime;
    }

    @JsonProperty("dtModifyTime")
    public Date getDtModifyTime() {
        return dtModifyTime;
    }

    public void setDtModifyTime(Date dtModifyTime) {
        this.dtModifyTime = dtModifyTime;
    }

    @JsonProperty("vCreateUserId")
    public String getVCreateUserId() {
        return vCreateUserId;
    }

    public void setVCreateUserId(String vCreateUserId) {
        this.vCreateUserId = vCreateUserId;
    }

    @JsonProperty("vModifyUserId")
    public String getVModifyUserId() {
        return vModifyUserId;
    }

    public void setVModifyUserId(String vModifyUserId) {
        this.vModifyUserId = vModifyUserId;
    }

}