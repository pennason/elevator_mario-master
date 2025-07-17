package com.shmashine.api.module.enoentnotice.input;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.shmashine.common.entity.base.PageListParams;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @PackgeName: com.shmashine.api.module.enoentnotice.input
 * @ClassName: SearchEnoentNoticeModule
 * @Date: 2020/7/1010:39
 * @Author: LiuLiFu
 * @Description: 查询事件列表 需关注电梯列表
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class SearchEnoentNoticeModule extends PageListParams {
    @NotNull(message = "请输入事件类型")
    private String noticeType;
    private String userId;

    private List<String> projectIds;
}
