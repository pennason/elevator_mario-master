package com.shmashine.pm.api.config.exception;

import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.dev33.satoken.current.SaGlobalException;

import com.shmashine.pm.api.entity.base.ResponseResult;

import lombok.extern.slf4j.Slf4j;

/**
 * 验证异常处理
 *
 * @author shmashine
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
        log.error("没有对参数进行json序列化异常:");
        return new ResponseResult(ResponseResult.CODE_VALID, "msg2_01");
    }


    /**
     * 没有对参数进行json序列化
     *
     * @return TODO 优化 验证String 类型的参数时 @NotBlank(message = " c 不能全部为空格") @NotEmpty(message = " c 不能为空字符串") 体验不佳
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseResult exceptionHandler(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        StringBuilder sb = new StringBuilder();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            sb.append(fieldError.getDefaultMessage());
        }
        return ResponseResult.resultValid(sb.toString());
    }
}
