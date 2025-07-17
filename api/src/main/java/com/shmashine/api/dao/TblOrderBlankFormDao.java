package com.shmashine.api.dao;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import com.shmashine.api.entity.TblOrderBlankForm;

@Service
public interface TblOrderBlankFormDao {
    public int insertRecord(@Param("tblOrderBlankForm") TblOrderBlankForm tblOrderBlankForm);

    public int insertBatch(@NotNull List<TblOrderBlankForm> list);

    public int deleteAllByOrderBlankId(String orderBlankId);
}
