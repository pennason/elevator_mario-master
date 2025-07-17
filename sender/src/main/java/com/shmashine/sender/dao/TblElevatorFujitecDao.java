package com.shmashine.sender.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.shmashine.common.entity.TblElevatorFujitec;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2024/04/26 17:29
 * @since v1.0
 */

@Mapper
public interface TblElevatorFujitecDao {

    //通过区号查询电梯信息
    List<TblElevatorFujitec> getElevatorByPtCode(String areaCity);

}
