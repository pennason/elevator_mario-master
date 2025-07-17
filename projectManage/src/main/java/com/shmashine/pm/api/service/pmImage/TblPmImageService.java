package com.shmashine.pm.api.service.pmImage;

import java.util.List;

import com.shmashine.pm.api.dao.TblPmImageDao;
import com.shmashine.pm.api.entity.TblPmImage;

public interface TblPmImageService {

    TblPmImageDao getTblPmImageDao();

    TblPmImage getById(String vInvestigateBillImageId);

    TblPmImage getByVInvestigateBillId(String vInvestigateBillId);

    int insert(TblPmImage tblPmImage);

    int insertBatch(List<TblPmImage> list);

    int update(TblPmImage tblPmImage);

    int updateBatch(List<TblPmImage> list);

    int deleteById(String vPmImageId);

    int deleteByTargetId(String vTargetId);
}
