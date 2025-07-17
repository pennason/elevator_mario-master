package com.shmashine.api.controller.system;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 版本标签配置文件返回实体
 *
 * @Author: jiangheng
 * @Version: 1.0.0
 * @Date: 2024/7/9 9:53
 * @Since: 1.0.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TagPropertiesRespVO {

    /**
     * 标签名-版本号
     */
    private String tagName;

    /**
     * 版本更新内容
     */
    private List<String> content;

}
