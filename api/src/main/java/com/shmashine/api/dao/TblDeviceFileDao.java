package com.shmashine.api.dao;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.shmashine.api.module.device.SearchDeviceFileModule;
import com.shmashine.api.module.device.UploadFileModule;
import com.shmashine.common.entity.TblDeviceFile;

/**
 * 设备升级文件管理
 *
 * @author little.li
 */
@Mapper
public interface TblDeviceFileDao {


    /**
     * 查询设备升级文件列表
     */
    List<Map<String, Object>> searchDeviceFileList(@Param("deviceFileModule") SearchDeviceFileModule deviceFileModule);


    TblDeviceFile getById(@NotNull String deviceFileId);

    List<TblDeviceFile> listByEntity(TblDeviceFile tblDeviceFile);

    List<TblDeviceFile> getByEntity(TblDeviceFile tblDeviceFile);

    List<TblDeviceFile> listByIds(@NotEmpty List<String> list);

    int insert(@NotNull TblDeviceFile tblDeviceFile);

    int insertBatch(@NotEmpty List<TblDeviceFile> list);

    int update(@NotNull TblDeviceFile tblDeviceFile);

    int updateByField(@NotNull @Param("where") TblDeviceFile where, @NotNull @Param("set") TblDeviceFile set);

    int updateBatch(@NotEmpty List<TblDeviceFile> list);

    int deleteById(@NotNull String deviceFileId);

    int deleteByEntity(@NotNull TblDeviceFile tblDeviceFile);

    int deleteByIds(@NotEmpty List<String> list);

    int countAll();

    int countByEntity(TblDeviceFile tblDeviceFile);

    TblDeviceFile getByDeviceFileName(@Param("fileName") String fileName);

    /**
     * 设备升级文件信息修改或删除
     *
     * @param uploadFileModule
     */
    void modifyOrDeleteFileInformation(@Param("uploadFileModule") UploadFileModule uploadFileModule);

    List<String> getHwVersions(@Param("eType") String eType);
}