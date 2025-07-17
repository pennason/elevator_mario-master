package com.shmashine.pm.api.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.shmashine.pm.api.entity.TblInvestigateBill;
import com.shmashine.pm.api.entity.dto.TblInvestigateBillDto;
import com.shmashine.pm.api.module.investigateBill.InvestigateBillModule;
import com.shmashine.pm.api.module.investigateBill.input.SearchTaskBillModule;

public interface BizInvestigateBillDao {

    /**
     * 小区列表
     */
    List<Map> searchInvestigateBillList(SearchTaskBillModule searchTaskBillModule);

//    /** 获取小区下拉框 */
//    List<Map> searchInvestigateTaskSelectList(SearchInvestigateTaskSelectListModule searchInvestigateTaskSelectListModule);

    TblInvestigateBill getByBillId(String vInvestigateBillId);

    Integer getElevatorCountByTaskId(@Param("vInvestigateTaskId") String vInvestigateTaskId, @Param("vInvestigateBillId") String vInvestigateBillId);

    List<Map> getAllBillByInvestigateTaskId(@Param("vInvestigateTaskId") String vInvestigateTaskId);

    List<Map> selectRelatedInfosByTaskId(@Param("vInvestigateTaskId") String vInvestigateTaskId);

    TblInvestigateBillDto getBillInfoByInvestigateBillId(@Param("vInvestigateBillId") String vInvestigateBillId);

    Map selectRelatedInfo(@Param("vInvestigateBillId") String vInvestigateBillId);

    List<Map> statusCountByVillageId(@Param("vVillageId") String vVillageId);

    List<Integer> getAllStatus(SearchTaskBillModule searchTaskBillModule);

    List<Map> selectByBillModule(InvestigateBillModule investigateBillModule);
}
