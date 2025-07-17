package com.shmashine.hikYunMou.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Builder;
import lombok.Data;

/**
 * @author jiangheng
 * @version 1.0
 * @date 2021/11/8 10:12
 */
@Builder
@Data
@TableName("tbl_sys_file")
public class FileDownloadTbl {

    /**
     * 主键.唯一标识
     */
    @TableId(type = IdType.INPUT)
    private String vFileId;

    /**
     * 文件类型（0：图片，1：视频）
     */
    private String vFileType;

    /**
     * 文件名
     */
    private String vFileName;

    /**
     * 业务id
     */
    private String vBusinessId;

    /**
     * 业务类型 1：工单，2：故障，3：电梯详情图片, 4:部门Log
     */
    private String iBusinessType;

    /**
     * 文件路径
     */
    private String vUrl;

    /**
     * 备注
     */
    private String vRemark;

    /**
     * 创建时间
     */
    private Date dtCreateTime;

    /**
     * 更新时间
     */
    private Date dtModifyTime;

    /**
     * 创建记录用户
     */
    private String v_create_user_id;

    /**
     * 修改记录用户
     */
    private String v_modify_user_id;


}
