package com.shmashine.api.config.exception;

import com.shmashine.api.entity.base.ResponseResult;

/**
 * @PackageName org.sulotion.config.exception
 * @ClassName BizException
 * @Date 2020/3/11 19:15
 * @Author Liulifu
 * Version v1.0
 * @description 自定义异常类
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
