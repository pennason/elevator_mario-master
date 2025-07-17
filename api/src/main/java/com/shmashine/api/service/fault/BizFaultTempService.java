package com.shmashine.api.service.fault;

import java.util.Map;

import com.shmashine.api.entity.base.PageListResultEntity;
import com.shmashine.api.module.fault.input.SearchFaultTempModule;
import com.shmashine.common.entity.TblFaultTemp;

/**
 * 故障记录临时表(TblFaultTemp)表服务接口
 *
 * @author makejava
 * @since 2020-06-29 18:40:09
 */
public interface BizFaultTempService {

    /**
     * 获取故障列表 - 分页
     */
    PageListResultEntity searchFaultSList(SearchFaultTempModule searchFaultTempModule);

    /**
     * 通过ID查询单条数据
     *
     * @param vFaultId 主键
     * @return 实例对象
     */
    TblFaultTemp queryById(String vFaultId);

    /**
     * 获取故障详情
     */
    Map<String, Object> getFaultTempDetail(String vFaultId);


    /**
     * 确认故障
     */
    void confirmFault(TblFaultTemp tblFaultTemp);

    /**
     * 取消故障
     */
    void cancelFault(TblFaultTemp tblFaultTemp);

    /**
     * 电梯停梯检测到有人
     */
    void faultConfirm(String faultId, int result);

    /**
     * 查询待确认的电动车乘梯
     */
    TblFaultTemp queryElectricMobileById(String faultId);

    /**
     * 电动车乘梯确认故障
     */
    void confirmElectricMobileFault(TblFaultTemp tblFaultTemp);

    /**
     * 电动车乘梯取消
     */
    void cancelElectricMobileFault(TblFaultTemp tblFaultTemp);

    /**
     * 每日凌晨恢复困人临时表中故障
     */
    void recoverPeopleTrappedFault();

    Boolean sendEntrap2Message(String faultId);
}