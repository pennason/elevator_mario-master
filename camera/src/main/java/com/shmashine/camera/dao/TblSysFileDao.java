package com.shmashine.camera.dao;


import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.shmashine.camera.entity.TblSysFile;
import com.shmashine.common.entity.TblFaultTemp;

@Mapper
public interface TblSysFileDao {

    TblSysFile getById(@NotNull String vFileId);

    TblSysFile getFileByFaultIdAnd(@Param("vBusinessId") String vBusinessId, @Param("vFileType") String vFileType);

    List<TblSysFile> listByEntity(TblSysFile tblSysFile);

    List<TblSysFile> getByEntity(TblSysFile tblSysFile);

    List<TblSysFile> listByIds(@NotEmpty List<String> list);

    int insert(@NotNull TblSysFile tblSysFile);

    int insertBatch(@NotEmpty List<String> list);

    int insertFileBatch(@NotEmpty @Param("list") List<TblSysFile> list);


    int update(@NotNull TblSysFile tblSysFile);

    int updateByField(@NotNull @Param("where") TblSysFile where, @NotNull @Param("set") TblSysFile set);

    int updateBatch(@NotEmpty List<TblSysFile> list);

    int deleteById(@NotNull String vFileId);

    int deleteByEntity(@NotNull TblSysFile tblSysFile);

    int deleteByIds(@NotEmpty List<String> list);

    int countAll();

    int countByEntity(TblSysFile tblSysFile);

    List<String> getByBusinessIdAndBusinessType(@Param("businessId") String businessId, @Param("businessType") int businessType);

    List<String> getByBusinessIdAndBusinessTypeAndFileType(@Param("businessId") String businessId,
                                                           @Param("businessType") int businessType,
                                                           @Param("fileType") int fileType);

    String getVideoUrl(@Param("businessId") String businessId,
                       @Param("businessType") int businessType,
                       @Param("fileType") int fileType);


    //Integer saveFile(String fileName);

    Integer saveFile(TblSysFile tblSysFile);

    /**
     * 根据故障id获取待审核故障
     *
     * @param faultId
     * @return
     */
    TblFaultTemp getFalutTempById(String faultId);

    /**
     * 获取故障审核图片
     *
     * @param faultId
     * @return
     */
    String getFaultTempImage(String faultId);
}
