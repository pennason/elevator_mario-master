package com.shmashine.api.service.village.impl;

import java.text.Collator;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.hutool.core.util.NumberUtil;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shmashine.api.controller.village.vo.VillagesAndPermissionStatusReqVO;
import com.shmashine.api.dao.BizVillageDao;
import com.shmashine.api.dao.TblElevatorDao;
import com.shmashine.api.dao.TblSysUserDao;
import com.shmashine.api.entity.base.PageListResultEntity;
import com.shmashine.api.module.village.input.SearchVillaListModule;
import com.shmashine.api.module.village.input.SearchVillaSelectListModule;
import com.shmashine.api.service.village.BizVillageService;
import com.shmashine.common.constants.SystemConstants;
import com.shmashine.common.entity.TblVillage;

/**
 * @PackgeName: com.shmashine.api.service.village.impl
 * @ClassName: BizVillageServiceImpl
 * @Date: 2020/7/611:12
 * @Author: LiuLiFu
 * @Description: 小区业务
 */
@Service
public class BizVillageServiceImpl implements BizVillageService {


    @Autowired
    BizVillageDao bizVillageDao;

    @Autowired
    private TblSysUserDao tblSysUserDao;

    @Autowired
    private TblElevatorDao tblElevatorDao;

    /**
     * 小区列表
     *
     * @param searchVillaSelectListModule
     * @return
     */
    @Override
    public PageListResultEntity<Map> searchVillageList(SearchVillaListModule searchVillaSelectListModule) {
        Integer pageIndex = searchVillaSelectListModule.getPageIndex();
        Integer pageSize = searchVillaSelectListModule.getPageSize();
        if (pageIndex == null || pageSize == null) {
            pageIndex = SystemConstants.DEFAULT_PAGE_INDEX;
            pageSize = SystemConstants.DEFAULT_PAGE_SIZE;
        }
        // 设置参数
        PageHelper.startPage(pageIndex, pageSize);
        PageInfo<Map> tPageInfo = new PageInfo<>(bizVillageDao.searchVillageList(searchVillaSelectListModule), pageSize);

        // 封装分页数据结构
        return new PageListResultEntity<>(pageIndex, pageSize, (int) tPageInfo.getTotal(), tPageInfo.getList());
    }

    @Override
    public PageListResultEntity<Map> searchVillageListWithElevator(SearchVillaListModule searchVillaSelectListModule) {
        Integer pageIndex = searchVillaSelectListModule.getPageIndex();
        Integer pageSize = searchVillaSelectListModule.getPageSize();
        if (pageIndex == null || pageSize == null) {
            pageIndex = SystemConstants.DEFAULT_PAGE_INDEX;
            pageSize = SystemConstants.DEFAULT_PAGE_SIZE;
        }
        // 设置参数
        PageHelper.startPage(pageIndex, pageSize);
        PageInfo<Map> tPageInfo = new PageInfo<>(bizVillageDao.searchVillageListWithElevator(searchVillaSelectListModule), pageSize);

        // 封装分页数据结构
        return new PageListResultEntity<>(pageIndex, pageSize, (int) tPageInfo.getTotal(), tPageInfo.getList());
    }

    /**
     * 获取所有小区结果
     *
     * @param searchVillaSelectListModule
     * @return
     */
    @Override
    public List<Map> searchAllVillage(SearchVillaListModule searchVillaSelectListModule) {

        return bizVillageDao.searchVillageList(searchVillaSelectListModule);

    }

    @Override
    public List<TblVillage> getVillageList(String userId, boolean isAdmin, String villageName) {

        return bizVillageDao.getVillageList(userId, isAdmin, villageName);
    }

    @Override
    public List<Map> searchVillagesByProject(VillagesAndPermissionStatusReqVO villagesReqVO) {

        /**
         * 获取所拥有电梯的小区
         */
        List<Map> maps = bizVillageDao.searchVillageSelectList(villagesReqVO);

        List<Map> res = maps.stream().map(it -> {


            //项目id
            String villageId = (String) it.get("v_village_id");

            //用户项目电梯授权状态 0:未授权;   大于0小于1:授权部分;  1:授权所有
            String permissionStatus = "0";
            //已经授权数
            Integer userCount = tblSysUserDao.countByVillageIdAndUser(villageId, villagesReqVO.getPermissionUserId());

            if (userCount > 0) {

                //电梯总数
                Integer count = tblElevatorDao.countByVillageId(villageId);

                permissionStatus = NumberUtil.div(userCount, count, 2).toPlainString();
            }

            //电梯授权情况
            it.put("permissionStatus", permissionStatus);

            return it;

        }).collect(Collectors.toList());

        //排序
        Comparator<Map> mapComparator = Comparator.comparing((Map o) -> (String) o.get("permissionStatus")).reversed()
                .thenComparing((Map o) -> (String) o.get("v_village_name"), Collator.getInstance(Locale.CHINA));

        return res.stream().sorted(mapComparator).collect(Collectors.toList());
    }


    /**
     * 小区下拉框
     *
     * @return
     */
    @Override
    public List<Map> searchVillageSelectList(SearchVillaSelectListModule searchVillaSelectListModule) {

        /**
         * 获取所拥有电梯的小区
         */
        return bizVillageDao.searchVillageSelectList(searchVillaSelectListModule);
    }

}
