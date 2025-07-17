package com.shmashine.api.controller.test;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shmashine.api.entity.base.ResponseResult;
import com.shmashine.api.service.test.TestServiceI;

/**
 * 分布地图接口
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @Resource
    private TestServiceI testService;

    @GetMapping("/list/{key}")
    public Object list(@PathVariable String key) {
        return ResponseResult.successObj(testService.list(key));
    }

    @GetMapping("/add/{key}")
    public Object add(@PathVariable String key) {
        testService.add(key);
        return ResponseResult.success();
    }

    @GetMapping("/delete")
    public Object delete(@PathVariable String key) {
        testService.delete(key);
        return ResponseResult.success();
    }

}
