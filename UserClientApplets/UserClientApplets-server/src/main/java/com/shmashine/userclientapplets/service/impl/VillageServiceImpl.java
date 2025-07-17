package com.shmashine.userclientapplets.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shmashine.userclientapplets.dao.VillageDao;
import com.shmashine.userclientapplets.entity.Village;
import com.shmashine.userclientapplets.service.UserService;
import com.shmashine.userclientapplets.service.VillageService;

/**
 * 小区服务实现
 *
 * @author jiangheng
 * @version V1.0 - 2022/2/17 15:46
 */
@Service
public class VillageServiceImpl extends ServiceImpl<VillageDao, Village> implements VillageService {

    @Autowired
    private UserService userService;

    @Override
    public List<Village> getVillageList(String userId, String villageName) {

        boolean admin = userService.isAdmin(userId);

        //根据用户电梯获取所有小区
        List<Village> villages = baseMapper.getVillageByDeptIds(userId, admin, villageName);

        return villages;
    }

    @Override
    public String getVillageNameByVillageId(String villageId) {
        Village village = getOne(new QueryWrapper<Village>().eq("v_village_id", villageId).last("LIMIT 1"));
        return village == null ? "" : village.getVVillageName();
    }

}
