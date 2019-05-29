package com.zhupeng.baseframe.exception;

import com.zhupeng.baseframe.common.ErrorCodeType;
import com.zhupeng.baseframe.common.ResponseStatus;
import com.zhupeng.baseframe.entity.common.CommonResponse;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 统一异常处理
 * @author zhupeng
 * @date 2018/12/11
 */
@RestControllerAdvice
public class ExceptionHelper {

    /**
     * 用于处理@Valid验证产生的异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public CommonResponse bindException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();

        //只取第一个错误信息
        String errorMesssage = "校验失败:" + bindingResult.getFieldErrors().get(0).getDefaultMessage();

        CommonResponse response = new CommonResponse();
        response.setException(true);
        response.setSuccess(false);
        response.setResponseStatus(new ResponseStatus(ErrorCodeType.VALID_ERROR.getErrorCode(),errorMesssage));
        return response;

    }

    /**
     * 该统一异常是未知异常，将该异常存入数据库，方便进行异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public CommonResponse bindException(Exception e) {
//        BindingResult bindingResult = e.getBindingResult();
//
//        //只取第一个错误信息
//        String errorMesssage = "校验失败:" + bindingResult.getFieldErrors().get(0).getDefaultMessage();
//
//        CommonResponse response = new CommonResponse();
//        response.setException(true);
//        response.setSuccess(false);
//        response.setResponseStatus(new ResponseStatus(ErrorCodeType.VALID_ERROR.getErrorCode(),errorMesssage));
//        return response;
        return null;
    }

}

