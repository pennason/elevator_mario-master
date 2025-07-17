package com.shmashine.api.module.user.input;

import java.util.List;

import javax.validation.constraints.Size;

/**
 * @PackgeName: com.shmashine.api.module.user.input
 * @ClassName: DeleteUserModule
 * @Date: 2020/6/916:35
 * @Author: LiuLiFu
 * @Description: 批量删除用户所需参数类
 */
public class DeleteBatchUserModule {

    @Size(min = 1, message = "最少选择一条用户数据")
    private List<String> arr;

    public List<String> getArr() {
        return arr;
    }

    public void setArr(List<String> arr) {
        this.arr = arr;
    }
}
