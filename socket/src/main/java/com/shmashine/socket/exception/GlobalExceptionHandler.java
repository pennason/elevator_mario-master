package com.shmashine.socket.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.dev33.satoken.current.SaGlobalException;
import cn.dev33.satoken.exception.NotLoginException;
import cn.hutool.core.exceptions.ExceptionUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 统一捕获异常 并封装返回
 *
 * @author jiangheng
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends SaGlobalException {

    /**
     * 未知异常
     */
    /*@ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResponseEntity exceptionHandler(Exception e) {
        log.error("发生未知异常！原因是:", e);
        return new ResponseEntity("发生未知异常，请稍后重试", HttpStatus.INTERNAL_SERVER_ERROR);
    }*/
    /**
     * 未知异常
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResponseEntity<String> exceptionHandler(Exception e) {
        log.error("发生未知异常, error {} {}", e.getMessage(), ExceptionUtil.stacktraceToString(e));
        if (e instanceof NotLoginException ee) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(e.getMessage());
        }
        return ResponseEntity.ok(e.getMessage());
    }

}
