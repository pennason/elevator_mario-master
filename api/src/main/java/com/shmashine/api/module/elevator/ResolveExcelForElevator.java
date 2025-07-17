package com.shmashine.api.module.elevator;

import java.util.ArrayList;
import java.util.List;

import com.shmashine.api.entity.ElevatorForExcel;
import com.shmashine.api.entity.SysUserResourceForExcel;
import com.shmashine.common.entity.TblDevice;

import lombok.Data;

/**
 * @AUTHOR jiangheng
 * @DATA 2021/3/4 - 15:20
 * 批量添加电梯excel解析结果类
 */
@Data
public class ResolveExcelForElevator {

    //可以添加的电梯列表
    List<ElevatorForExcel> elevatorList = new ArrayList<>();

    //添加失败的电梯code
    List<String> failElevatorList = new ArrayList<>();

    //需要授权的SysUserResource资源权限表
    List<SysUserResourceForExcel> sysUserResourceList = new ArrayList<>();

    //添加电梯关联设备
    List<TblDevice> addDeviceList = new ArrayList<>();

}
