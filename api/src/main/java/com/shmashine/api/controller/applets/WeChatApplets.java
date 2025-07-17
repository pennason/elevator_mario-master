package com.shmashine.api.controller.applets;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shmashine.api.entity.base.BaseRequestEntity;
import com.shmashine.userclient.client.RemoteUserClientAppletsClient;
import com.shmashine.userclient.vo.WeChatUserUpdateReqVO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "微信小程序", description = "小程序相关-开发者：")
@RestController
@RequestMapping("/applets")
public class WeChatApplets extends BaseRequestEntity {

    @Autowired
    private RemoteUserClientAppletsClient remoteUserClientAppletsClient;

    @Operation(summary = "查询微信用户")
    @PostMapping("/queryWeChatUser")
    public Object queryWeChatUser(@RequestParam(value = "pageIndex") Integer pageIndex,
                                  @RequestParam(value = "pageSize") Integer pageSize,
                                  @RequestParam(value = "userId", required = false) String userId,
                                  @RequestParam(value = "userName", required = false) String userName,
                                  @RequestParam(value = "phoneNumber", required = false) String phoneNumber,
                                  @RequestParam(value = "role", required = false) String role,
                                  @RequestParam(value = "appName", required = false) String appName,
                                  @RequestParam(value = "isRegister", required = false) Integer isRegister) {

        return remoteUserClientAppletsClient.queryWeChatUser(pageIndex, pageSize, userId, userName, phoneNumber, role, appName, isRegister);
    }

    /**
     * 微信用户 通过|拒绝|修改|删除
     *
     * @param reqVO 请求体
     * @return ResponseResult
     */
    @Operation(summary = "更新微信用户 通过|拒绝|修改|删除")
    @PostMapping("/updateWeChatUserInfo")
    public Object updateWeChatUserInfo(@Valid @RequestBody WeChatUserUpdateReqVO reqVO) {

        return remoteUserClientAppletsClient.updateWeChatUserInfo(reqVO);
    }

    /**
     * 获取授权电梯
     *
     * @param permission 是否授权 0:未授权，1：已授权，2：全部
     * @return
     */
    @PostMapping("/getPermissionElevators")
    public Object getPermissionElevators(@RequestParam("permission") Integer permission,
                                         @RequestParam("userId") String userId,
                                         @RequestParam(value = "villageId", required = false) String villageId,
                                         @RequestParam(value = "vProjectId", required = false) String vProjectId) {
        return remoteUserClientAppletsClient.getPermissionElevators(permission, userId, villageId, vProjectId, getUserId());
    }

}
