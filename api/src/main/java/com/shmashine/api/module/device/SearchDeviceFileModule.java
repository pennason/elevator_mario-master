package com.shmashine.api.module.device;

import com.shmashine.common.entity.base.PageListParams;

/**
 * @author little.li
 */
public class SearchDeviceFileModule extends PageListParams {

    /**
     * 硬件版本号
     */
    private String hWVersion;

    private String eType;

    public String gethWVersion() {
        return hWVersion;
    }

    public void sethWVersion(String hWVersion) {
        this.hWVersion = hWVersion;
    }

    public String geteType() {
        return eType;
    }

    public void seteType(String eType) {
        this.eType = eType;
    }
}
