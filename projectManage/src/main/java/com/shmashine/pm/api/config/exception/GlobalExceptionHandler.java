package com.shmashine.pm.api.config.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;

import cn.dev33.satoken.exception.NotLoginException;
import cn.hutool.core.exceptions.ExceptionUtil;

import com.shmashine.pm.api.entity.base.ResponseResult;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ValidExceptionHandler {

    /**
     * 处理空指针的异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = NullPointerException.class)
    @ResponseBody
    public ResponseResult exceptionHandler(NullPointerException e) {
        log.error("发生空指针异常！原因是:", e);
        return ResponseResult.error();
    }

    /**
     * 处理请求方法不支持的异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    public ResponseResult exceptionHandler(HttpRequestMethodNotSupportedException e) {
        log.error("发生请求方法不支持异常！原因是:", e);
        return new ResponseResult("0001", "msg1_03");
    }

    /**
     * 404异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = NoHandlerFoundException.class)
    @ResponseBody
    public ResponseResult exceptionHandler(NoHandlerFoundException e) {
        log.error("访问Api不存在！原因是:", e);
        return ResponseResult.error();
    }


    /**
     * by zero异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = ArithmeticException.class)
    @ResponseBody
    public ResponseResult exceptionHandler(ArithmeticException e) {
        log.error("发生by zero异常！原因是:", e);
        return ResponseResult.error();
    }

    /**
     * 转换整数异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = NumberFormatException.class)
    @ResponseBody
    public ResponseResult exceptionHandler(NumberFormatException e) {
        log.error("发生 NumberFormatException 转换异常！原因是:", e);
        return ResponseResult.error();
    }


    /**
     * 捕获自定义异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = BizException.class)
    @ResponseBody
    public ResponseResult exceptionHandler(BizException e) {
        log.error("业务手动抛出异常！");
        return e.getCauseEntity();
    }

    /**
     * 文件上传过大
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = MaxUploadSizeExceededException.class)
    @ResponseBody
    public ResponseResult exceptionHandler(MaxUploadSizeExceededException e) {
        log.error("文件上传失败，文件超出服务器上传文件大小:", e);
        return new ResponseResult("0001", "msg1_09");
    }

    /**
     * 未知异常
     *
     * @param e
     * @return
     */
    /*@ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResponseResult exceptionHandler(Exception e) {
        log.error("发生未知异常！原因是:", e);
        return ResponseResult.error();
    }*/
    /**
     * 未知异常
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResponseEntity<ResponseResult> exceptionHandler(Exception e) {
        log.error("发生未知异常, error {} {}", e.getMessage(), ExceptionUtil.stacktraceToString(e));
        if (e instanceof NotLoginException ee) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ResponseResult.error(e.getMessage()));
        }
        return ResponseEntity.ok(ResponseResult.error(e.getMessage()));
    }
}
