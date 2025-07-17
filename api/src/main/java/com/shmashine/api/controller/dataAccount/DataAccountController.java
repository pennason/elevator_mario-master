package com.shmashine.api.controller.dataAccount;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shmashine.api.entity.TblDataAccount;
import com.shmashine.api.entity.base.BaseRequestEntity;
import com.shmashine.api.entity.base.ResponseResult;
import com.shmashine.api.module.dataAccount.SearchDataAccountModule;
import com.shmashine.api.service.dataAccount.BizDataAccountService;
import com.shmashine.api.service.dataAccount.TblDataAccountService;
import com.shmashine.common.utils.SnowFlakeUtils;

@RequestMapping("/dataAccount")
@RestController
public class DataAccountController extends BaseRequestEntity {

    @Autowired
    private TblDataAccountService tblDataAccountService;
    @Autowired
    private BizDataAccountService bizDataAccountService;

    @PostMapping("/search")
    public Object list(@RequestBody SearchDataAccountModule searchDataAccountModule) {
        searchDataAccountModule.setUserId(getUserId());
        return ResponseResult.successObj(bizDataAccountService.search(searchDataAccountModule));
    }

    @PostMapping("/insert")
    public Object insert(@RequestBody TblDataAccount tblDataAccount) {
        String accountId = SnowFlakeUtils.nextStrId();
        tblDataAccount.setvDataAccountId(accountId);
        int suc = tblDataAccountService.insert(tblDataAccount);
        if (suc > 0) {
            return ResponseResult.success();
        } else {
            return ResponseResult.error();
        }
    }

    @PostMapping("/update")
    public Object update(@RequestBody TblDataAccount tblDataAccount) {
        int suc = tblDataAccountService.update(tblDataAccount);
        if (suc > 0) {
            return ResponseResult.success();
        } else {
            return ResponseResult.error();
        }
    }

    @PostMapping("/delete")
    public Object delete(@RequestBody TblDataAccount tblDataAccount) {
        int suc = tblDataAccountService.deleteById(tblDataAccount.getvDataAccountId());
        if (suc > 0) {
            return ResponseResult.success();
        } else {
            return ResponseResult.error();
        }
    }
}
