package com.shmashine.camera.model.base;

import java.util.List;

@SuppressWarnings("rawtypes")
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
    private Integer totalRecordCnt;

    /**
     * 数据List
     */
    private List list;

    public PageListResultEntity() {
    }

    public PageListResultEntity(Integer pageIndex, Integer pageSize, Integer totalRecordCnt, List<T> list) {
        super();
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
        this.totalRecordCnt = totalRecordCnt;
        this.list = list;
    }

    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotalRecordCnt() {
        return totalRecordCnt;
    }

    public void setTotalRecordCnt(Integer totalRecordCnt) {
        this.totalRecordCnt = totalRecordCnt;
    }

    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }

}
