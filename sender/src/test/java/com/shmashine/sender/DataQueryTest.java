package com.shmashine.sender;

import java.util.List;

import javax.annotation.Resource;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.shmashine.sender.entity.TblFaultShield;
import com.shmashine.sender.server.fault.TblFaultShieldService;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2024/04/26 17:29
 * @since v1.0
 */

@SpringBootTest
public class DataQueryTest {

    @Resource
    private TblFaultShieldService faultShieldService;

    @Test
    void test() {
        List<TblFaultShield> faultShieldList = faultShieldService.getFaultShieldByElevatorCode("MX3681");
        var jsonArray = JSON.parseArray(JSON.toJSONString(faultShieldList), JSONArray.class);
        System.out.println("faultShieldList is " + JSON.toJSONString(jsonArray));
    }
}
