// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.api.controller.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shmashine.api.entity.base.BaseRequestEntity;
import com.shmashine.api.service.system.TblSysUserWecomServiceI;
import com.shmashine.common.dto.PlatformWecomUserRelationRequestDTO;
import com.shmashine.common.dto.ResponseResultDTO;
import com.shmashine.common.entity.TblSysUserWecomEntity;
import com.shmashine.wecom.api.servlet.gateway.WeComGateway;
import com.shmashine.wecom.components.dto.department.WeComDepartmentBaseResponseDTO;
import com.shmashine.wecom.components.dto.user.WeComUserSimpleListResponseDTO;
import com.shmashine.wecom.components.properties.WeComBaseProperties;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/5/25 18:28
 * @since v1.0
 */

@Slf4j
@RestController
@RequestMapping("/user/wecome")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@Tag(name = "企业微信通讯录接口", description = "企业微信通讯录相关接口 - powered by ChenXue")
@SecurityScheme(name = "token", scheme = "bearer", type = SecuritySchemeType.HTTP)
public class UserWecomController extends BaseRequestEntity {
    private final WeComGateway weComGateway;
    private final TblSysUserWecomServiceI userWecomService;

    // 本地操作数据库

    /**
     * 存储平台用户ID与企业微信用户之间关系
     */
    @Operation(summary = "存储平台用户ID与企业微信用户之间关系", security = {@SecurityRequirement(name = "token")})
    @PostMapping("/relation/save")
    public ResponseResultDTO<TblSysUserWecomEntity> saveUserRelation(
            @RequestBody PlatformWecomUserRelationRequestDTO requestDTO) {
        var res = userWecomService.save(TblSysUserWecomEntity.builder()
                .userId(requestDTO.getUserId())
                .wecomUserId(requestDTO.getWecomUserId())
                .wecomUserName(requestDTO.getWecomUserName())
                .createBy(getUserId())
                .wecomCorpId(WeComBaseProperties.CORP_ID)
                .wecomCorpName(WeComBaseProperties.CORP_NAME)
                .wecomAgentId(String.valueOf(WeComBaseProperties.MONITOR_APP_AGENT_ID))
                .build());
        return ResponseResultDTO.successObj(res);
    }


    // 远程调用企业微信的接口

    /**
     * 根据部门ID获取子部门列表，部门ID如果传0，默认获取全量组织架构
     */
    @Operation(summary = "根据部门ID获取子部门列表", description = "如果传0，默认获取全量组织架构")
    @Parameters({
            @Parameter(name = "id", description = "部门ID", required = true, example = "0")
    })
    @GetMapping("/department/list/{id}")
    public ResponseResultDTO<List<WeComDepartmentBaseResponseDTO>> getDepartmentList(@PathVariable("id") Integer id) {
        var token = weComGateway.getToken(WeComBaseProperties.MONITOR_APP_AGENT_ID);
        var res = weComGateway.getDepartmentList(token, id);
        if (res.getCode() != 0) {
            log.error("获取部门列表失败，错误码：{}，错误信息：{}", res.getCode(), res.getMessage());
            return ResponseResultDTO.error(res.getMessage());
        }
        return ResponseResultDTO.successObj(res.getDepartments());
    }

    /**
     * 根据部门ID获取成员基本列表，部门ID为1时是全部成员, fetchChild是否递归获取子部门下面的成员
     */
    @Operation(summary = "根据部门ID获取成员基本列表", description = "部门ID为1时是全部成员, fetchChild是否递归获取子部门下面的成员（1：递归）")
    @Parameters({
            @Parameter(name = "departmentId", description = "部门ID,全部请设置为1", required = true, example = "1"),
            @Parameter(name = "fetchChild", description = "是否递归获取子部门下面的成员（1：递归）", required = true, example = "1")
    })
    @GetMapping("/user/simple/list/{departmentId}/{fetchChild}")
    public ResponseResultDTO<List<WeComUserSimpleListResponseDTO.UserSimpleInfo>> getUserSimpleList(
            @PathVariable("departmentId") Integer departmentId, @PathVariable("fetchChild") Integer fetchChild) {
        var token = weComGateway.getToken(WeComBaseProperties.MONITOR_APP_AGENT_ID);
        var res = weComGateway.getUserSimpleList(token, departmentId, fetchChild);
        if (res.getCode() != 0) {
            log.error("获取部门成员列表失败，错误码：{}，错误信息：{}", res.getCode(), res.getMessage());
            return ResponseResultDTO.error(res.getMessage());
        }
        return ResponseResultDTO.successObj(res.getUserList());
    }


}
