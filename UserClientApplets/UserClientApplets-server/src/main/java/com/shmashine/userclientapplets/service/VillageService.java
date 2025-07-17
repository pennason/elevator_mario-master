package com.shmashine.userclientapplets.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shmashine.userclientapplets.entity.Village;

/**
 * 小区服务
 *
 * @author jiangheng
 * @version V1.0.0 - 2022/2/17 15:44
 */
public interface VillageService extends IService<Village> {

    /**
     * 获取小区列表
     */
    List<Village> getVillageList(String userId, String villageName);

    /**
     * 根据id获取小区name
     *
     * @param villageId 小区id
     * @return 小区名
     */
    String getVillageNameByVillageId(String villageId);
}
