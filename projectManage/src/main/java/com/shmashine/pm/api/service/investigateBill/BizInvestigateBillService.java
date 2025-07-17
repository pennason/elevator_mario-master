package com.shmashine.pm.api.service.investigateBill;

import java.util.List;
import java.util.Map;

import com.shmashine.pm.api.entity.TblInvestigateBill;
import com.shmashine.pm.api.entity.base.PageListResultEntity;
import com.shmashine.pm.api.entity.dto.TblInvestigateBillDto;
import com.shmashine.pm.api.module.investigateBill.InvestigateBillModule;
import com.shmashine.pm.api.module.investigateBill.input.SearchTaskBillModule;

public interface BizInvestigateBillService {

//    List<Map> searchInvestigateBillSelectList(SearchInvestigateBillSelectListModule searchInvestigateBillSelectListModule);

    PageListResultEntity<Map> searchInvestigateBillList(SearchTaskBillModule searchTaskBillModule);

    List<Map> searchAllInvestigateBill(SearchTaskBillModule searchTaskBillModule);

    TblInvestigateBill getByBillId(String vInvestigateBillId);

    Integer getElevatorCountByTaskId(String vInvestigateTaskId, String vInvestigateBillId);

    List<Map> getAllBillByInvestigateTaskId(String vInvestigateTaskId);

    TblInvestigateBillDto getBillInfoByInvestigateBillId(String vInvestigateBillId);

    List<Map> selectRelatedInfosByTaskId(String vInvestigateTaskId);

    Map selectRelatedInfo(String vInvestigateBillId);

    List<Map> statusCountByVillageId(String vVillageId);

    List<Integer> getAllStatus(SearchTaskBillModule searchTaskBillModule);

    List<Map> selectByBillModule(InvestigateBillModule investigateBillModule);
}
