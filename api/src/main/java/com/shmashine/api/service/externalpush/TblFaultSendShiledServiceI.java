package com.shmashine.api.service.externalpush;

import java.util.List;

import com.shmashine.api.dao.TblFaultSendShiledDao;
import com.shmashine.common.entity.TblFaultSendShiled;

public interface TblFaultSendShiledServiceI {

    TblFaultSendShiledDao getTblFaultSendShiledDao();

    TblFaultSendShiled getById(String vFaultSendShiledId);

    List<TblFaultSendShiled> getByEntity(TblFaultSendShiled tblFaultSendShiled);

    List<TblFaultSendShiled> listByEntity(TblFaultSendShiled tblFaultSendShiled);

    List<TblFaultSendShiled> listByIds(List<String> ids);

    int insert(TblFaultSendShiled tblFaultSendShiled);

    int insertBatch(List<TblFaultSendShiled> list);

    int update(TblFaultSendShiled tblFaultSendShiled);

    int updateBatch(List<TblFaultSendShiled> list);

    int deleteById(String vFaultSendShiledId);

    int deleteByEntity(TblFaultSendShiled tblFaultSendShiled);

    int deleteByIds(List<String> list);

    int countAll();

    int countByEntity(TblFaultSendShiled tblFaultSendShiled);
}