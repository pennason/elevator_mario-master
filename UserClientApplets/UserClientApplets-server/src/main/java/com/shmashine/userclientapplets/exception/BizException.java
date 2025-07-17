package com.shmashine.userclientapplets.exception;

import com.shmashine.userclientapplets.entity.ResponseResult;

/**
 * 自定义BizException
 *
 * @author Liulifu
 * @version v1.0.0 - 2020/3/11 19:15
 */
public class BizException extends RuntimeException {

    private ResponseResult causeEntity;

    public BizException(ResponseResult cause) {
        this.causeEntity = cause;
    }

    public ResponseResult getCauseEntity() {
        return causeEntity;
    }

    public void set_causeEntity(ResponseResult causeEntity) {
        this.causeEntity = causeEntity;
    }
}
