package com.shmashine.api.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.shmashine.api.entity.base.SequenceRange;

/**
 * 采番
 */
public interface BizSequenceDao {
    /**
     * 查询采番编号
     */
    Map selectKey(@Param("keyName") String keyName);

    /**
     * 初次生成编号
     */
    Integer insertKey(@Param("keyName") String keyName);

    /**
     * 初次生成编号（多个）
     */
    Integer insertKeyIDRange(@Param("keyName") String keyName, @Param("count") String count);

    /**
     * 更新最新编号
     */
    Integer updateKey(@Param("keyName") String keyName);

    /**
     * 更新最新编号 （多个）
     */
    Integer updateKeyIDRange(@Param("keyName") String keyName, @Param("count") int count);

    /**
     * 查询范围编号
     */
    List<SequenceRange> selectKeyIDRange(@Param("keyName") String keyName, @Param("count") int count);


}
