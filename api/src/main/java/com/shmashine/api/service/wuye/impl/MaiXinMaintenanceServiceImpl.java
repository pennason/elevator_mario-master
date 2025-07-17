package com.shmashine.api.service.wuye.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.shmashine.api.dao.BizElevatorDao;
import com.shmashine.api.dao.BizRuiJinDao;
import com.shmashine.api.dao.MaiXinWuyeMaintenanceDao;
import com.shmashine.api.entity.base.PageListResultEntity;
import com.shmashine.api.module.fault.input.SearchFaultModule;
import com.shmashine.api.service.village.TblVillageServiceI;
import com.shmashine.api.service.wuye.MaiXinMaintenanceService;
import com.shmashine.common.constants.SystemConstants;

@Service
public class MaiXinMaintenanceServiceImpl implements MaiXinMaintenanceService {

    @Autowired
    private MaiXinWuyeMaintenanceDao baseMapper;
    @Autowired
    private BizRuiJinDao bizRuiJinDao;
    @Autowired
    private BizElevatorDao bizElevatorDao;
    @Autowired
    private TblVillageServiceI tblVillageServiceI;

    @Override
    public Integer getMaintenanceCount(SearchFaultModule searchFaultModule) {
        return baseMapper.getMaintenanceCount(searchFaultModule);
    }

    @Override
    public Integer getOverdueMaintenanceCount(SearchFaultModule searchFaultModule) {
        return baseMapper.getOverdueMaintenanceCount(searchFaultModule);
    }

    /**
     * 获取维保记录列表
     *
     * @param searchFaultModule
     * @return
     */
    @Override
    public PageListResultEntity searchMaintenanceList(SearchFaultModule searchFaultModule) {
        // 维保超期的记录数
        List<Map> overdueOrders = Lists.newArrayList();
        //正常维保
        List<Map> normalOrders = Lists.newArrayList();

        List<Map> resultList = Lists.newArrayList();

        List<Map> list = baseMapper.queryMaintenanceWorkOrdersList(searchFaultModule);

        for (int i = 0; i < list.size(); i++) {
            Date nextMaintenanceDate = (Date) list.get(i).get("next_maintenance_date");
            Date completeData = (Date) list.get(i).get("complete_data");
            if (completeData.after(nextMaintenanceDate)) {
                // 超期维保记录
                Map overdueOrder = list.get(i);
                overdueOrder.put("overdue", 1);
                overdueOrders.add(overdueOrder);
            } else {
                //正常维保
                Map normalOrder = list.get(i);
                normalOrder.put("overdue", 0);
                normalOrders.add(normalOrder);
            }
        }

        //只有一条默认为正常维保
        if (list.size() == 1) {
            //正常维保
            Map normalOrder = list.get(0);
            normalOrder.put("overdue", 0);
            normalOrders.add(normalOrder);
        }


        /*---------------------------------------*/
        if (searchFaultModule.getOverdue() == null) {    //所有
            resultList.addAll(normalOrders);
            resultList.addAll(overdueOrders);
        } else {
            if (searchFaultModule.getOverdue() == 0) {      //正常
                resultList.addAll(normalOrders);
            } else if (searchFaultModule.getOverdue() == 1) {    //超期
                resultList.addAll(overdueOrders);
            } else if (searchFaultModule.getOverdue() == 2) {        //待年检 = 总梯数 - 已年检梯
                //获取所有电梯
                List<Map<String, Object>> elevators = bizElevatorDao.searchElevatorByUserId(searchFaultModule.isAdminFlag, searchFaultModule.userId);

                //已年检电梯
                List<String> elevatorCodes = normalOrders.stream().map(it -> (String) it.get("v_elevator_code")).collect(Collectors.toList());

                //构建参数
                List<HashMap<String, Object>> orders = elevators.stream().filter(it -> !elevatorCodes.contains(it.get("v_elevator_code"))).map(
                        it -> {
                            HashMap<String, Object> res = new HashMap<>();
                            //电梯编号
                            res.put("v_elevator_code", it.get("v_elevator_code"));
                            //电梯注册码
                            res.put("register_number", it.get("register_number"));
                            //安装地址
                            res.put("v_address", it.get("v_address"));
                            //计划维保日期
                            res.put("should_complete_date", it.get("d_next_inspect_date"));
                            //工单状态 待年检
                            res.put("overdue", "2");
                            //开始时间
                            res.put("sign_time", "");
                            //完成时间
                            res.put("complete_time", "");
                            //类型
                            res.put("order_type_number", "BYLX04");
                            //维保人员
                            res.put("deal_employee_name", it.get("v_maintain_person_name"));
                            //联系方式
                            res.put("deal_employee_tel", it.get("v_maintain_person_tel"));
                            res.put("v_elevator_name", it.get("v_elevator_name"));
                            res.put("villageId", it.get("villageId"));

                            return res;
                        }
                ).collect(Collectors.toList());

                resultList.addAll(orders);
            }
        }

        Integer pageIndex = searchFaultModule.getPageIndex();
        Integer pageSize = searchFaultModule.getPageSize();
        if (pageIndex == null || pageSize == null) {
            pageIndex = SystemConstants.DEFAULT_PAGE_INDEX;
            pageSize = SystemConstants.DEFAULT_PAGE_SIZE;
        }
        // 设置参数
        PageHelper.startPage(pageIndex, pageSize);
        int s = pageSize * (pageIndex - 1);
        int e = pageSize * pageIndex;
        List<Map> maps;

        if (resultList.size() > pageSize) {
            e = resultList.size() < e ? resultList.size() : e;
            maps = resultList.subList(s, e);
        } else {
            maps = resultList;
        }

        // 扩展小区信息
        tblVillageServiceI.extendVillageInfo(maps);

        PageInfo<Map> mapPageInfo = new PageInfo<>(maps, pageSize);
        // 封装分页数据结构
        PageListResultEntity<Map> mapPageListResultEntity = new PageListResultEntity<>(pageIndex, pageSize, resultList.size(), mapPageInfo.getList());
        return mapPageListResultEntity;
    }
}
