package com.shmashine.userclientapplets.entity;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分页查询返回实体
 *
 * @param <T> 数据类型
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageListResultEntity<T> {

    /**
     * 页数
     */
    private Integer pageIndex;

    /**
     * 每页行数
     */
    private Integer pageSize;

    /**
     * 总数据行数
     */
    private Long totalRecordCnt;

    /**
     * 数据List
     */
    private List<T> list;

}
