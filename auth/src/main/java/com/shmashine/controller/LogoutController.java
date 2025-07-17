package com.shmashine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONObject;
import com.shmashine.entity.ResultEntity;
import com.shmashine.util.StringUtil;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class LogoutController {

    @Autowired
    @Qualifier("consumerTokenServices")
    ConsumerTokenServices tokenServices;

    /**
     * 用户退出登录接口<br>
     *
     * @param param json字符串<br>
     *              参数样例：<br>
     *              { <br>
     *              "head": { "request_time": "2018-07-13 11:00:19", "user_id":
     *              "admin" },<br>
     *              "body": { "accessToken":
     *              "1dc3158f-a45e-4183-b2d2-afe48f1c29dc" } <br>
     *              }
     * @return Json对象<br>
     * 返回结果样例：<br>
     * { "resCode": "0000",<br>
     * "resMessage": "处理成功！",<br>
     * "resInfo": {<br>
     * "head": { "request_time": "2018-07-13 11:00:19", "user_id":
     * "admin" },<br>
     * "body": { "accessToken": "1dc3158f-a45e-4183-b2d2-afe48f1c29dc" }
     * <br>
     * }<br>
     * }<br>
     */
    @PostMapping("/logout")
    public Object logout(@RequestBody String param) {
        log.debug("logout Parameter = " + param);
        ResultEntity result = null;
        JSONObject json = null;
        try {
            json = JSONObject.parseObject(param);
            JSONObject body = json.getJSONObject("body");
            String accessToken = body.getString("accessToken");

            if (StringUtil.isEmpty(accessToken)) {
                // 参数为空，返回错误：ApiMsg7005=缺少必要的参数[%]！
                result = new ResultEntity("7005", "ApiMsg7005", new String[]{"accessToken"}, null);
            } else {
                // 验证通过，做退出登录处理
                // 1. DB中记录退出登录时间。 TODO

                // 2. 从Redis中删除accessToken。
                tokenServices.revokeToken(accessToken);
                result = new ResultEntity(ResultEntity.RESULT_CODE_OK, "ApiMsg0000", null, json);
            }
        } catch (JSONException ej) {
            // ApiMsg7004=非法的json参数！
            result = new ResultEntity("7004", "ApiMsg7004", null, null);
        } catch (Exception e) {
            // ApiMsg7001=处理失败！
            result = new ResultEntity("7001", "ApiMsg7001", null, e.getMessage());
        }
        log.debug("logout result = " + result.toJson());
        return result;
    }

    @PostMapping("/test")
    public Object test(@RequestBody String param) {
        return "ok";
    }
}
