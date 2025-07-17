package com.shmashine.api.module.device;

import java.util.List;

import lombok.Data;

/**
 * @AUTHOR jiangheng
 * @DATA 2021/3/8 - 16:24
 * <p>
 * 设备批量升级文件
 */
@Data
public class UploadDeviceFileModuleBatch {

    /**
     * 升级文件id
     */
    private String deviceFileId;

    /**
     * 升级电梯信息
     */
    private List<UploadDeviceFileModule> uploadDeviceFileModules;
}
