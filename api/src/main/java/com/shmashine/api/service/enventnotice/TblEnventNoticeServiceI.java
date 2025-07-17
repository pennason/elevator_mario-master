package com.shmashine.api.service.enventnotice;

import java.util.List;

import com.shmashine.api.dao.TblEnventNoticeDao;
import com.shmashine.common.entity.TblEnventNotice;

public interface TblEnventNoticeServiceI {

    TblEnventNoticeDao getTblEnventNoticeDao();

    TblEnventNotice getById(String vEnventNotifyId);

    List<TblEnventNotice> getByEntity(TblEnventNotice tblEnventNotice);

    List<TblEnventNotice> listByEntity(TblEnventNotice tblEnventNotice);

    List<TblEnventNotice> listByIds(List<String> ids);

    int insert(TblEnventNotice tblEnventNotice);

    int insertBatch(List<TblEnventNotice> list);

    int update(TblEnventNotice tblEnventNotice);

    int updateBatch(List<TblEnventNotice> list);

    int deleteById(String vEnventNotifyId);

    int deleteByEntity(TblEnventNotice tblEnventNotice);

    int deleteByIds(List<String> list);

    int countAll();

    int countByEntity(TblEnventNotice tblEnventNotice);
}