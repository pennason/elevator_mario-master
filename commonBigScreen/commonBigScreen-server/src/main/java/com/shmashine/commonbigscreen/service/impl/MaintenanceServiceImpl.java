package com.shmashine.commonbigscreen.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shmashine.commonbigscreen.client.RemoteApiClient;
import com.shmashine.commonbigscreen.dao.MaintenanceDao;
import com.shmashine.commonbigscreen.entity.Maintenance;
import com.shmashine.commonbigscreen.entity.SearchElevatorModule;
import com.shmashine.commonbigscreen.entity.SearchFaultModule;
import com.shmashine.commonbigscreen.service.MaintenanceService;

/**
 * MaintenanceServiceImpl
 *
 * @author jiangheng
 * @version V1.0 - 2022/3/14 13:53
 */
@Service
public class MaintenanceServiceImpl extends ServiceImpl<MaintenanceDao, Maintenance> implements MaintenanceService {

    @Autowired
    private RemoteApiClient remoteApiClient;

    @Override
    public HashMap<String, Object> getMaintenanceOverdueElevator(SearchElevatorModule searchElevatorModule) {

        //获取维保逾期电梯
        var jsonObject = JSON.parseObject(JSON.toJSONString(searchElevatorModule));
        //1:周，2：月
        Integer modeStatus = searchElevatorModule.getiModeStatus();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());

        if (modeStatus == 1) {
            c.add(Calendar.DATE, -7);
        } else {
            c.add(Calendar.DATE, -30);
        }
        String dtReportTime = format.format(c.getTime());

        jsonObject.put("dtReportTime", dtReportTime);
        //获取超期记录
        jsonObject.put("overdue", 1);
        jsonObject.put("pageIndex", 1);
        jsonObject.put("pageSize", 100);
        HashMap responseMaintenanceList =
                remoteApiClient.searchMaintenanceList(jsonObject, searchElevatorModule.getUserId());

        HashMap<String, Object> responseResult = new HashMap<>(3);
        responseResult.put("lable", "维保逾期电梯");
        responseResult.put("number", responseMaintenanceList.get("totalRecordCnt"));

        ArrayList<HashMap<String, Object>> list = (ArrayList) responseMaintenanceList.get("list");
        String name = "";
        for (HashMap<String, Object> it : list) {
            String elevatorCode = (String) it.get("v_elevator_code");
            name = StringUtils.join(name, ",", elevatorCode);
        }
        if (StringUtils.isNotEmpty(name)) {
            name = name.substring(1);
        }
        responseResult.put("name", name);

        return responseResult;
    }

    @Override
    public HashMap<String, Object> getYearCheckOverdueElevator(SearchElevatorModule searchElevatorModule) {

        //获取年检逾期电梯
        var jsonObject = JSON.parseObject(JSON.toJSONString(searchElevatorModule));

        //1:周，2：月
        Integer modeStatus = searchElevatorModule.getiModeStatus();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());

        if (modeStatus == 1) {
            c.add(Calendar.DATE, -7);
        } else {
            c.add(Calendar.DATE, -30);
        }
        String dtReportTime = format.format(c.getTime());
        jsonObject.put("dtReportTime", dtReportTime);
        jsonObject.put("overdue", 1);
        //设置年检类型
        jsonObject.put("iFaultType", "BYLX04");
        HashMap responseYearCheckList =
                remoteApiClient.searchMaintenanceList(jsonObject, searchElevatorModule.getUserId());

        HashMap<String, Object> responseResult = new HashMap<>(3);
        responseResult.put("lable", "年检逾期电梯");
        responseResult.put("number", responseYearCheckList.get("totalRecordCnt"));

        ArrayList<HashMap<String, Object>> list = (ArrayList) responseYearCheckList.get("list");

        String name = "";
        for (HashMap<String, Object> it : list) {
            String elevatorCode = (String) it.get("v_elevator_code");
            name = StringUtils.join(name, ",", elevatorCode);
        }

        if (StringUtils.isNotEmpty(name)) {
            name = name.substring(1);
        }
        responseResult.put("name", name);

        return responseResult;
    }

    @Override
    public Integer getMaintenanceCount(SearchFaultModule searchFaultModule) {

        return baseMapper.getMaintenanceCount(searchFaultModule);

    }

    @Override
    public List<Map> getOverdueOrders(String elevatorId) {
        return baseMapper.getOverdueOrders(elevatorId);
    }
}
