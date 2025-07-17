package com.shmashine.userclientapplets.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shmashine.userclientapplets.client.RemoteApiClient;
import com.shmashine.userclientapplets.dao.MaintenanceDao;
import com.shmashine.userclientapplets.entity.Maintenance;
import com.shmashine.userclientapplets.entity.SearchFaultModule;
import com.shmashine.userclientapplets.service.MaintenanceService;

/**
 * 维保服务实现
 *
 * @author jiangheng
 * @version V1.0.0 - 2022/2/15 11:20
 */
@Service
public class MaintenanceServiceImpl extends ServiceImpl<MaintenanceDao, Maintenance> implements MaintenanceService {

    @Autowired
    private RemoteApiClient remoteApiClient;

    @Override
    public Maintenance getMaintenanceById(String workOrderNumber) {

        Maintenance maintenance = getOne(new QueryWrapper<Maintenance>()
                .select("register_number", "work_order_number", "sign_time", "complete_time",
                        "should_complete_date", "deal_employee_name", "deal_employee_tel", "order_type_number")
                .eq("work_order_number", workOrderNumber));

        return maintenance;
    }

    @Override
    public HashMap queryMaintenanceList(SearchFaultModule searchFaultModule) {

        var jsonObject = JSON.parseObject(JSON.toJSONString(searchFaultModule), JSONObject.class);
        jsonObject.put("dtReportTime", searchFaultModule.getStartTime());
        jsonObject.put("dtEndTime", searchFaultModule.getEndTime());
        return remoteApiClient.searchMaintenanceList(jsonObject, searchFaultModule.getUserId());
    }

    @Override
    public String queryMaintenanceByDay(String userId, boolean admin) {
        return baseMapper.queryMaintenanceByDay(userId, admin);
    }

    @Override
    public List<Map> getOverdueOrders(String elevatorId) {
        return baseMapper.getOverdueOrders(elevatorId);
    }

    @Override
    public HashMap queryAnnualInspectionList(SearchFaultModule searchFaultModule) {

        var jsonObject = JSON.parseObject(JSON.toJSONString(searchFaultModule), JSONObject.class);
        //待年检梯
        if (searchFaultModule.getOverdue() != null && searchFaultModule.getOverdue() == 2) {
            return remoteApiClient.getUnAnnualInspectionElevatorByPage(jsonObject, searchFaultModule.getUserId());
        } else {     //年检记录
            return remoteApiClient.queryRuiJinAnnualCheckList(jsonObject, searchFaultModule.getUserId());
        }

    }
}
