package com.shmashine.socket.file.service;

import java.util.List;

/**
 * 文件接口
 *
 * @author little.li
 */
public interface FileService {


    /**
     * 保存文件路径
     *
     * @param fileName 文件名称
     * @param faultId  关联的故障id
     */
    void saveVideoFile(String fileName, String faultId);


    /**
     * 保存文件路径
     *
     * @param fileName 文件名称
     * @param faultId  关联的故障id
     */
    void saveImageFile(List<String> fileName, String faultId);


    /**
     * 保存文件路径
     *
     * @param path    文件名称
     * @param faultId 关联的故障id
     */
    void saveFile(String path, String faultId);
}
