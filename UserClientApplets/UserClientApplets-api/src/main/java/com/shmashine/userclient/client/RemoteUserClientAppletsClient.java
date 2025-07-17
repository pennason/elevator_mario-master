package com.shmashine.userclient.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.shmashine.userclient.config.FeignClientFormPostConfig;
import com.shmashine.userclient.entity.ResponseResult;
import com.shmashine.userclient.vo.WeChatUserUpdateReqVO;

/**
 * 小程序远程调用api
 */
@Component
@FeignClient(url = "${endpoint.shmashine-applets:shmashine-applets:8080}",
        name = "shmashine-applets", configuration = FeignClientFormPostConfig.class)
public interface RemoteUserClientAppletsClient {

    @PostMapping("/applets/weChatLogin/queryWeChatUser")
    ResponseResult queryWeChatUser(@RequestParam(value = "pageIndex") Integer pageIndex,
                                   @RequestParam(value = "pageSize") Integer pageSize,
                                   @RequestParam(value = "userId", required = false) String userId,
                                   @RequestParam(value = "userName", required = false) String userName,
                                   @RequestParam(value = "phoneNumber", required = false) String phoneNumber,
                                   @RequestParam(value = "role", required = false) String role,
                                   @RequestParam(value = "appName", required = false) String appName,
                                   @RequestParam(value = "isRegister", required = false) Integer isRegister);


    @PostMapping("/applets/weChatLogin/updateWeChatUserInfo")
    ResponseResult updateWeChatUserInfo(@RequestBody WeChatUserUpdateReqVO req);

    @PostMapping("/applets/elevator/getPermissionElevators")
    ResponseResult getPermissionElevators(@RequestParam("permission") Integer permission,
                                          @RequestParam("userId") String userId,
                                          @RequestHeader("villageId") String villageId,
                                          @RequestHeader("vProjectId") String vProjectId,
                                          @RequestHeader("user_id") String requestUserId);

}
