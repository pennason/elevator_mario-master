package com.shmashine.api.service.file;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.shmashine.api.dao.TblSysFileDao;
import com.shmashine.common.entity.TblSysFile;

public interface TblSysFileServiceI {

    TblSysFileDao getTblSysFileDao();

    TblSysFile getById(String vFileId);

    List<TblSysFile> getByEntity(TblSysFile tblSysFile);

    List<TblSysFile> listByEntity(TblSysFile tblSysFile);

    List<TblSysFile> listByIds(List<String> ids);

    int insert(TblSysFile tblSysFile);

    void insertWorkOrderBatch(List<String> fileNameList, String workOrderDetailId);

    int update(TblSysFile tblSysFile);

    int updateBatch(List<TblSysFile> list);

    int deleteById(String vFileId);

    int deleteByEntity(TblSysFile tblSysFile);

    int deleteByIds(List<String> list);

    int countAll();

    int countByEntity(TblSysFile tblSysFile);

    /**
     * @param businessId   业务id
     * @param businessType 业务类型 1：工单，2：故障，3：电梯详情图片
     * @param fileType     文件类型,0:图片，1:视频，2:bin(设备相关文件)，3:excel
     * @return
     */
    String getVideoUrl(@Param("businessId") String businessId,
                       @Param("businessType") int businessType,
                       @Param("fileType") int fileType);

    /**
     * 根据业务id获取文件列表
     *
     * @param businessId 业务id
     * @return 文件列表
     */
    List<TblSysFile> getByBusinessId(String businessId);
}