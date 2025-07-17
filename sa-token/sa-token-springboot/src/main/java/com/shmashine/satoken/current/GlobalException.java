package com.shmashine.satoken.current;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.dev33.satoken.current.AjaxJson;
import cn.dev33.satoken.current.SaGlobalException;
import cn.dev33.satoken.exception.NotLoginException;
import cn.hutool.core.exceptions.ExceptionUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 全局异常处理
 *
 * @author jiangheng
 */
@Slf4j
@ControllerAdvice
public class GlobalException extends SaGlobalException {

    /**
     * 未知异常
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResponseEntity<AjaxJson> exceptionHandler(Exception e) {
        log.error("SaToken, 发生未知异常, error {} {}", e.getMessage(), ExceptionUtil.stacktraceToString(e));
        if (e instanceof NotLoginException ee) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(AjaxJson.getNotLogin().setData(ee.getMessage()));
        }
        return ResponseEntity.ok(AjaxJson.getError(e.getMessage()));
    }

}
