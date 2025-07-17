package com.shmashine.api.dao;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.shmashine.common.entity.TblSysFile;

@Mapper
public interface TblSysFileDao {

    TblSysFile getById(@NotNull String vFileId);

    List<TblSysFile> listByEntity(TblSysFile tblSysFile);

    List<TblSysFile> getByEntity(TblSysFile tblSysFile);

    List<TblSysFile> listByIds(@NotEmpty List<String> list);

    int insert(@NotNull TblSysFile tblSysFile);

    int insertBatch(List<TblSysFile> list);

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

    List<TblSysFile> getAllByBusinessIdAndBusinessType(@Param("businessId") String businessId, @Param("businessType") int businessType);

    /**
     * 根据业务id获取文件列表
     *
     * @param businessId 业务id
     * @return 文件列表
     */
    List<TblSysFile> getByBusinessId(String businessId);
}