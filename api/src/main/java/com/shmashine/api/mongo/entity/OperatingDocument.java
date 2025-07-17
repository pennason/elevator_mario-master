package com.shmashine.api.mongo.entity;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 客服展示（操作文档）
 *
 * @Author: jiangheng
 * @Version: 1.0.0
 * @Date: 2024/8/16 16:51
 * @Since: 1.0.0
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class OperatingDocument {

    @Id
    private String id;

    /**
     * 安装示意图
     */
    private List<DocumentFile> installationDiagram;

    /**
     * 设备实体安装图
     */
    private List<DocumentFile> deviceInstallationDiagram;

    /**
     * 各种功能展示现场视频
     */
    private List<DocumentFile> functionDisplayVideo;

    /**
     * 各区域安装点地图
     */
    private List<DocumentFile> installationPointMaps;

    /**
     * 使用过程中人为破坏 污秽 设备图片及视频(各种常见故障)
     */
    private List<DocumentFile> commonQuestion;

    /**
     * 文件名称&地址
     */
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class DocumentFile implements Serializable {
        private String fileName;
        private String filePath;
    }
}
