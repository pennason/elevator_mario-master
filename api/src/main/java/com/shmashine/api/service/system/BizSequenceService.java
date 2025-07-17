package com.shmashine.api.service.system;

import java.util.List;

import com.shmashine.api.entity.base.SequenceRange;

/**
 * 采番
 */
public interface BizSequenceService {

    /**
     * 获取最大编号
     */
    Integer getNewSequenceID(String keyName);

    /**
     * 获取多个可用的最大编号
     */
    SequenceRange GetNewSequenceIDRange(String keyName, int count);

    /**
     * 获取一个最新主键
     */
    String getSequenceId(String key, int digit);

    /**
     * 获取多个最新可用的主键
     */
    List<String> getSequenceIdList(String key, int digit, int count);


}
