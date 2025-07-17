package com.shmashine.pm.api.config.exception;

import com.shmashine.pm.api.entity.base.ResponseResult;

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
