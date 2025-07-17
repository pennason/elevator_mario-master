// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.sender.dao;

import java.util.Collection;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.shmashine.common.entity.TblProject;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/1/17 11:25
 * @since v1.0
 */

@Mapper
public interface TblProjectDao {

    List<TblProject> listProjectByIds(Collection<String> projectIds);
}
