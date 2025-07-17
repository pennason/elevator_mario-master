package com.shmashine.api.controller.haierDianxin;

import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.dao.SaTokenDaoRedisFastjson2;
import cn.dev33.satoken.stp.StpUtil;

import com.alibaba.fastjson2.JSON;
import com.shmashine.api.entity.TblHaierLiftInfo;
import com.shmashine.api.entity.TblHaierProject;
import com.shmashine.api.service.haierCamera.HaierCameraService;

/**
 * 海尔电信推送接口
 *
 * @author jiangheng
 */
@RestController
@RequestMapping("/haier/dianxin")
public class HaierDianxinController {

    @Autowired
    public RedisTemplate redisTemplate;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HaierCameraService haierCameraService;

    @Autowired
    private RestTemplate restTemplate;

    @Resource
    private SaTokenDaoRedisFastjson2 saTokenDao;

    //海尔推送梯缓存
    private static final String HAIERCAMERA_TOKEN = "haier:camera:sendDianxin:token";
    //梯信息缓存
    private static final String HAIERCAMERA_LIFTINFO_KEY = "haier:camera:liftInfo";
    //推送项目信息
    private static final String PUSH_PROJECT_INFO_URL
            = "https://icity.sh.189.cn:8881/hjdata-server/project/postProject";
    //推送梯信息
    private static final String PUSH_LIFT_INFO_URL = "https://icity.sh.189.cn:8881/hjdata-server/lift/postLift";

    @SaIgnore
    @RequestMapping("/getToken")
    public ResponseEntity getRealVideoAuthorization(@RequestBody HashMap<String, String> hashMap) {

        String userName = hashMap.get("userName");
        String passWord = hashMap.get("passWord");

        if (!"haier".equals(userName)) {
            return new ResponseEntity("用户名错误", HttpStatus.resolve(403));
        }
        if (!"haier123456".equals(passWord)) {
            return new ResponseEntity("密码错误", HttpStatus.resolve(403));
        }

        //用户登录
        String token = StpUtil.login("haier");

        //添加权限列表
        List<String> menuIds = Arrays.asList("haier:pushProjectInfo", "haier:pushLiftInfo");
        saTokenDao.reloadPermissionList("haier", menuIds);

        HashMap<String, Object> res = new HashMap<>();
        res.put("token", token);
        res.put("expires", 1800);

        return ResponseEntity.ok(res);
    }

    /**
     * 推送项目信息
     */
    @SaCheckPermission("haier:pushProjectInfo")
    @RequestMapping("/pushProjectInfo")
    public ResponseEntity pushProjectInfo(@RequestBody List<TblHaierProject> projects) {

        long time = System.currentTimeMillis() / 1000;
        Set set = redisTemplate.opsForZSet().rangeByScore(HAIERCAMERA_TOKEN, time - 1800, time);
        redisTemplate.opsForZSet().removeRangeByScore(HAIERCAMERA_TOKEN, 0, time - 1801);

        String token = request.getHeader("token");

        for (Object o : set) {
            if (token.equals(o)) {

                //添加数据库
                haierCameraService.insertAndUpdataProjects(projects);

                //推送松江电信
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.setContentType(MediaType.APPLICATION_JSON);
                httpHeaders.set("authorization", "Basic " + Base64.getEncoder()
                        .encodeToString("haier:85@abZ#*@sd#08R".getBytes()));
                HttpEntity<List<TblHaierProject>> httpEntity = new HttpEntity<>(projects, httpHeaders);
                String result = restTemplate.postForObject(PUSH_PROJECT_INFO_URL, httpEntity, String.class);

                return ResponseEntity.ok(result);
            }
        }

        return new ResponseEntity("token失效", HttpStatus.resolve(401));
    }

    /**
     * 推送电梯基本信息
     */
    @SaCheckPermission("haier:pushLiftInfo")
    @RequestMapping("/pushLiftInfo")
    public ResponseEntity pushLiftInfo(@RequestBody HashMap<String, Object> hashMap) {

        long time = System.currentTimeMillis() / 1000;
        Set set = redisTemplate.opsForZSet().rangeByScore(HAIERCAMERA_TOKEN, time - 1800, time);
        redisTemplate.opsForZSet().removeRangeByScore(HAIERCAMERA_TOKEN, 0, time - 1801);

        String token = request.getHeader("token");

        for (Object o : set) {
            if (token.equals(o)) {

                String projectId = String.valueOf(hashMap.get("projectId"));

                TblHaierProject tblHaierProject = haierCameraService.getProjectById(projectId);

                var tblHaierLiftInfos = JSON.parseArray(JSON.toJSONString(hashMap.get("lifts")),
                        TblHaierLiftInfo.class);

                HashMap<String, TblHaierLiftInfo> cacheMap = new HashMap<>();
                for (TblHaierLiftInfo tblHaierLiftInfo : tblHaierLiftInfos) {
                    //根据电梯编号获取设备号

                    var deviceNo = haierCameraService.getDeviceNoByRegistrationCode(
                            tblHaierLiftInfo.getRegistrationCode());
                    tblHaierLiftInfo.setDeviceNo(deviceNo == null ? "null" : deviceNo);

                    tblHaierLiftInfo.setProjectId(projectId);
                    tblHaierLiftInfo.setProjectName(tblHaierProject.getName());
                    cacheMap.put(String.valueOf(tblHaierLiftInfo.getRegistrationCode()), tblHaierLiftInfo);
                }
                hashMap.put("lifts", tblHaierLiftInfos);
                //添加数据库
                haierCameraService.insertAndUpdataLiftInfos(tblHaierLiftInfos);

                //更新缓存
                redisTemplate.opsForHash().putAll(HAIERCAMERA_LIFTINFO_KEY, cacheMap);
                //推送松江电信
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.setContentType(MediaType.APPLICATION_JSON);
                httpHeaders.set("authorization", "Basic " + Base64.getEncoder()
                        .encodeToString("haier:85@abZ#*@sd#08R".getBytes()));
                HttpEntity<HashMap<String, Object>> httpEntity = new HttpEntity<>(hashMap, httpHeaders);
                String result = restTemplate.postForObject(PUSH_LIFT_INFO_URL, httpEntity, String.class);

                return ResponseEntity.ok(result);
            }
        }

        return new ResponseEntity("token失效", HttpStatus.resolve(401));
    }

}
