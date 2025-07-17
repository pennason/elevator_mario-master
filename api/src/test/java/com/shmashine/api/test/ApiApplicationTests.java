package com.shmashine.api.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import cn.hutool.crypto.SecureUtil;

import com.shmashine.api.service.elevator.BizElevatorService;

@SpringBootTest
class ApiApplicationTests {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    private BizElevatorService elevatorService;

    @Test
    void contextLoads() {

        //appid(908dfrgw4erdfg)+iccid(8986112027804431367)+key(shmxsj)
        String str = "908dfrgw4erdfg" + "8986112027804431331" + "shmxsj";
        String md5Str = SecureUtil.md5(str);
        System.out.println(md5Str);


        Map<String, Object> map = new HashMap<>();
        map.put("sign", md5Str);
        map.put("appId", "908dfrgw4erdfg");
        map.put("iccid", "8986112027804431331");
        //String message = JSON.toJSONString(map);

//        String url = "http://iot.aipaas.com/api/card/query/detail";

        String url = "http://iot.aipaas.com/api/card/query/flow/package";

        // 发出一个post请求
        try {
            HashMap s = restTemplate.postForObject(url, map, HashMap.class);

            System.out.println("s = " + s);
        } catch (RestClientException e) {
            e.printStackTrace();

        }
    }

    @Test
    void textupdate() {

        List<String> iccidList = elevatorService.searchAllIotCardIccid();

        int batchCount = 10;
        ArrayList<String> tempList = new ArrayList<>();
        int iccidSize = iccidList.size();
        for (int i = 0; i < iccidSize; i++) {
            tempList.add(iccidList.get(i));
            if ((i + 1) % batchCount == 0 || (i + 1) == iccidSize) {
                elevatorService.updateIotCardInfo(tempList);
                tempList.clear();
            }
        }
    }

    @Test
    void textupdateRunCount() {
        elevatorService.searchAllRunCount();
    }

}
