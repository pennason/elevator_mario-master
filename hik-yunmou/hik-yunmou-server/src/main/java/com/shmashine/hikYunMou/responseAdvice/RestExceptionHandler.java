package com.shmashine.hikYunMou.responseAdvice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import cn.dev33.satoken.current.SaGlobalException;
import cn.dev33.satoken.exception.NotLoginException;
import cn.hutool.core.exceptions.ExceptionUtil;

import com.shmashine.hikYunMou.exception.MyException;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author jiangheng
 * @create 2022/9/3 16:23
 */

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler extends SaGlobalException {


    /**
     * 统一异常处理方法，@ExceptionHandler(RuntimeException.class)声明这个方法处理RuntimeException这样的异常
     *
     * @param e 捕获到的异常
     * @return 返回给页面的状态码和信息
     */
    @ExceptionHandler(MyException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResultData<String> handleMyException(MyException e) {

        log.error("自定义异常信息! ex={}", e.getMessage(), e);

        return ResultData.fail(e.getStatus(), e.getMessage());
    }

    /**
     * 全局异常处理
     *
     * @param e
     * @return
     */
    /*@ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResultData<String> exception(Exception e) {
        log.error("全局异常信息! ex={}", e.getMessage(), e);
        return ResultData.fail(ReturnCode.RC500.getCode(), e.getMessage());
    }*/
    /**
     * 未知异常
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResponseEntity<ResultData<String>> exceptionHandler(Exception e) {
        log.error("发生未知异常, error {} {}", e.getMessage(), ExceptionUtil.stacktraceToString(e));
        if (e instanceof NotLoginException ee) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ResultData.fail(ReturnCode.RC500.getCode(), e.getMessage()));
        }
        return ResponseEntity.ok(ResultData.fail(ReturnCode.RC500.getCode(), e.getMessage()));
    }


    //////////////////////////////////////////

    /**
     * 处理空指针的异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = NullPointerException.class)
    @ResponseBody
    public ResultData exceptionHandler(NullPointerException e) {
        log.error("发生空指针异常！原因是:", e);
        return ResultData.fail(ReturnCode.RC500.getCode(), e.getMessage());
    }

    /**
     * 处理请求方法不支持的异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    public ResultData exceptionHandler(HttpRequestMethodNotSupportedException e) {
        log.error("发生请求方法不支持异常！原因是:", e);
        return ResultData.fail(ReturnCode.RC500.getCode(), "请求方法不支持!");
    }

//    /**
//     * 404异常
//     * @param e
//     * @return
//     */
//    @ExceptionHandler(value = NoHandlerFoundException.class)
//    @ResponseBody
//    public ResultData exceptionHandler(NoHandlerFoundException e){
//        log.error("访问Api不存在！原因是:",e);
//        return ResultData.fail(ReturnCode.RC500.getCode(),e.getMessage());
//    }
//
//
//
//    /**
//     * by zero异常
//     * @param e
//     * @return
//     */
//    @ExceptionHandler(value =ArithmeticException.class)
//    @ResponseBody
//    public ResultData exceptionHandler(ArithmeticException e){
//        log.error("发生by zero异常！原因是:",e);
//        return ResultData.fail(ReturnCode.RC500.getCode(),e.getMessage());
//    }
//
//    /**
//     * 转换整数异常
//     * @param e
//     * @return
//     */
//    @ExceptionHandler(value =NumberFormatException.class)
//    @ResponseBody
//    public ResultData exceptionHandler(NumberFormatException e){
//        log.error("发生 NumberFormatException 转换异常！原因是:",e);
//        return ResultData.fail(ReturnCode.RC500.getCode(),e.getMessage());
//    }
//
//
//    /**
//     * 文件上传过大
//     * @param e
//     * @return
//     */
//    @ExceptionHandler(value = MaxUploadSizeExceededException.class)
//    @ResponseBody
//    public ResultData exceptionHandler(MaxUploadSizeExceededException e){
//        log.error("文件上传失败，文件超出服务器上传文件大小:",e);
//        return ResultData.fail(ReturnCode.RC500.getCode(),e.getMessage());
//    }
//
//    /**
//     * 未知异常
//     * @param e
//     * @return
//     */
//    @ExceptionHandler(value = Exception.class)
//    @ResponseBody
//    public ResultData exceptionHandler(Exception e){
//        log.error("发生未知异常！原因是:",e);
//        return ResultData.fail(ReturnCode.RC500.getCode(),e.getMessage());
//    }
//
//
//
//
//
//    /**
//     * 无法连接数据异常
//     * @param e
//     * @return
//     */
////    @ExceptionHandler(value =CannotGetJdbcConnectionException.class)
////    @ResponseBody
////    public ResponseResult exceptionHandler(CannotGetJdbcConnectionException e){
////        log.error("无法连接数据库！");
////        return ResponseResult.error();
////    }
//
//    /**
//     * 处理必须输入
//     * @param e
//     * @return
//     */
//    @ExceptionHandler(value = MissingServletRequestParameterException.class)
//    @ResponseBody
//    public ResultData exceptionHandler(MissingServletRequestParameterException e){
//        log.error("请输入:",e);
//        return ResultData.fail(ReturnCode.RC500.getCode(),e.getMessage());
//    }
//
//
//
//    /**
//     * 没有对参数进行json序列化
//     * @param e
//     * @return
//     */
//    @ExceptionHandler(value = HttpMessageNotReadableException.class)
//    @ResponseBody
//    public ResultData exceptionHandler(HttpMessageNotReadableException e){
//        log.error("没有对参数进行json序列化异常:");
//        return ResultData.fail(ReturnCode.RC500.getCode(),e.getMessage());
//    }
//
//
//    /**
//     * 没有对参数进行json序列化
//     * @param e
//     * @return
//     */
//    @ExceptionHandler(value = MethodArgumentNotValidException.class)
//    @ResponseBody
//    public ResultData exceptionHandler(MethodArgumentNotValidException e){
//        BindingResult bindingResult = e.getBindingResult();
//        StringBuffer sb = new StringBuffer();
//        for(FieldError fieldError :bindingResult.getFieldErrors()){
//            sb.append(fieldError.getDefaultMessage());
//        }
//        return ResultData.fail(ReturnCode.RC500.getCode(),e.getMessage());
//    }

}
