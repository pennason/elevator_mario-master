package com.shmashine.userclientapplets.exception;

import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.dev33.satoken.current.SaGlobalException;

import com.shmashine.userclientapplets.entity.ResponseResult;

import lombok.extern.slf4j.Slf4j;

/**
 * 捕获参数认证失败异常  ！验证需要追加并优化，友好提示
 *
 * @author Liulifu
 * @version V1.0.0 - 2020/3/11 19:36
 */
@Slf4j
public class ValidExceptionHandler extends SaGlobalException {

    /**
     * 处理必须输入
     */
    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    @ResponseBody
    public ResponseResult exceptionHandler(MissingServletRequestParameterException e) {
        log.error("请输入:", e);
        return ResponseResult.error();
    }


    /**
     * 没有对参数进行json序列化
     */
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    @ResponseBody
    public ResponseResult exceptionHandler(HttpMessageNotReadableException e) {
        log.error("没有对参数进行json序列化异常:", e);
        return new ResponseResult(ResponseResult.CODE_VALID, "msg2_01");
    }


    /**
     * 没有对参数进行json序列化
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseResult exceptionHandler(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        StringBuffer sb = new StringBuffer();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            sb.append(fieldError.getDefaultMessage());
        }
        return ResponseResult.resultValid(sb.toString());
    }


}
