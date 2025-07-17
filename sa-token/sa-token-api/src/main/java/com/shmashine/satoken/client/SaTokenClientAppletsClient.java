package com.shmashine.satoken.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.shmashine.satoken.dto.SaResultDTO;

/**
 * saToken-client 远程调用
 *
 * @author jiangheng
 */
@Component
@FeignClient(url = "${endpoint.shmashine-satoken:shmashine-satoken:8080}", name = "shmashine-satoken")
//@FeignClient(value = "shmashine-satoken")
public interface SaTokenClientAppletsClient {

    /**
     * 用户登录
     *
     * @param name 用户名
     * @param pwd  密码
     * @return 登录结果
     */
    @PostMapping("/user/doLogin")
    SaResultDTO doLogin(@RequestParam("name") String name, @RequestParam("pwd") String pwd);

    /**
     * 重新加载权限
     *
     * @param userId 用户id
     * @return 重新加载结果
     */
    @PostMapping("/user/reloadPermission")
    SaResultDTO reloadPermission(@RequestParam("userId") String userId);
}
