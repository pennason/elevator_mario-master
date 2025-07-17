package com.shmashine.userclientapplets.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 小区实体
 *
 * @author jiangheng
 * @version V1.0.0 - 2022/2/17 15:45
 */
@TableName("tbl_village")
@Data
@EqualsAndHashCode(callSuper = false)
public class Village extends BaseEntity {

    /**
     * 小区id
     */
    private String vVillageId;

    /**
     * 小区名字
     */
    private String vVillageName;

    /**
     * 小区详细地址
     */
    private String vAddress;

    /**
     * 小区所属项目id
     */
    private String vProjectId;

    /**
     * 经度
     */
    private String vLongitude;

    /**
     * 纬度
     */
    private String vLatitude;
}
