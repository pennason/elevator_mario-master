package com.shmashine.api.controller.system;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.dev33.satoken.annotation.SaIgnore;

import com.shmashine.api.config.tag.TagProperties;
import com.shmashine.api.entity.base.BaseRequestEntity;
import com.shmashine.api.entity.base.ResponseResult;
import com.shmashine.api.module.system.GetAreaModule;
import com.shmashine.api.module.system.GetSelectModule;
import com.shmashine.api.mongo.entity.OperatingDocument;
import com.shmashine.api.mongo.utils.MongoTemplateUtil;
import com.shmashine.api.service.system.BizSystemService;

import lombok.extern.slf4j.Slf4j;

/**
 * 系统字典下拉框接口
 */
@RestController
@Slf4j
@RequestMapping("/select")
public class BizSystemController extends BaseRequestEntity {

    @Autowired
    BizSystemService bizSystemService;

    @Resource
    private MongoTemplateUtil mongoTemplateUtil;

    /**
     * 获取系统下拉框 int 类型
     *
     * @param getSelectModule
     * @return
     */
    @PostMapping("getSelectList")
    public Object getSelectList(@RequestBody @Valid GetSelectModule getSelectModule) {
        List<Map> selectList = bizSystemService.getSelectList(getSelectModule.getMainId());
        return selectList;
    }


    /**
     * 获取系统下拉框 原始 string 类型
     *
     * @param getAreaModule
     * @return
     */
    @PostMapping("getPlatformSelectList")
    public Object getPlatformSelectList(@RequestBody @Valid GetSelectModule getAreaModule) {
        List<Map> selectList = bizSystemService.platformSelectList(getAreaModule.getMainId());
        return selectList;
    }

    /**
     * 获取特殊的工单状态
     *
     * @param getAreaModule
     * @return
     */
    @PostMapping("getWorkOrderSelectList")
    public Object getWorkOrderSelectList(@RequestBody @Valid GetSelectModule getAreaModule) {
        List<Map> selectList = bizSystemService.getWorkStatusSelectList(getAreaModule.getMainId());
        return selectList;
    }


    /**
     * 获取省一级信息
     *
     * @return
     */
    @PostMapping("getAreaInfo")
    public Object getAreaInfo() {
        // 无需传入参数
        List<Map> selectList = bizSystemService.getAreaSelectList("");
        return selectList;
    }

    /**
     * 根据编号获取下级市区信息
     *
     * @param getAreaModule
     * @return
     */
    @PostMapping("getAreaInfoNext")
    public Object getAreaInfoNext(@RequestBody @Valid GetAreaModule getAreaModule) {
        List<Map> selectList = bizSystemService.getAreaSelectList(getAreaModule.getAreaId());
        return selectList;
    }

    /**
     * 获取故障标准
     *
     * @return
     */
    @GetMapping("/getFaultDefinitionSelectList")
    public ResponseResult getFaultDefinitionSelectList(@RequestParam(value = "projectIds", required = false) String projectIdsString,
                                                       @RequestParam(value = "villageIds", required = false) String villageIdsString) {
        // 不可使用 stream 的 collect, jdk 不兼容
        ArrayList<String> projectIds = new ArrayList<>();
        if (org.springframework.util.StringUtils.hasText(projectIdsString)) {
            Arrays.stream(projectIdsString.split(",")).map(String::trim)
                    .forEach(projectIds::add);
        }
        ArrayList<String> villageIds = new ArrayList<>();
        if (org.springframework.util.StringUtils.hasText(villageIdsString)) {
            Arrays.stream(villageIdsString.split(",")).map(String::trim)
                    .forEach(villageIds::add);
        }
        return ResponseResult.successObj(bizSystemService.getFaultDefinitionSelectList(getUserId(), projectIds, villageIds));
    }

    /**
     * 获取当前版本信息
     *
     * @return 版本信息
     */
    @SaIgnore
    @GetMapping("/getTagInfo")
    public ResponseResult getTagInfo() {
        TagPropertiesRespVO respVO = TagPropertiesRespVO.builder()
                .tagName(TagProperties.TAG_NAME)
                .content(TagProperties.CONTENT).build();
        return ResponseResult.successObj(respVO);
    }

    /**
     * 获取客服展示-操作文档
     *
     * @return 版本信息
     */
    @GetMapping("/getOperatingDocument")
    public ResponseResult getOperatingDocument() {

        OperatingDocument operatingDocument = mongoTemplateUtil.findById("1", OperatingDocument.class);
        return ResponseResult.successObj(operatingDocument);

    }

}
