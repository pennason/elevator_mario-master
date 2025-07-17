package com.shmashine.api.dao;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.shmashine.common.entity.TblSysMenu;

public interface BizUserMapper {

    /**
     * 用户列表
     */
    ArrayList<LinkedHashMap> searchUserList(TblSysMenu tblSysMenu);

}
