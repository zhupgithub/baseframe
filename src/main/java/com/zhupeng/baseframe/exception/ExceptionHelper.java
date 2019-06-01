package com.zhupeng.baseframe.exception;

import com.zhupeng.baseframe.entity.ro.ResponseResult;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class ExceptionHelper {

    /**
     * 用于处理@Valid验证产生的异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseResult bindException(MethodArgumentNotValidException e) {
        log.error(e.getMessage());
        BindingResult bindingResult = e.getBindingResult();

        //只取第一个错误信息
        String errorMesssage = "校验失败:" + bindingResult.getFieldErrors().get(0).getDefaultMessage();

        return ResponseResult.error(ResponseResult.SYSTEM_EXCEPTION , errorMesssage);

    }

    /**
     * 该统一异常是未知异常，将该异常存入数据库，方便进行异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ResponseResult bindException(Exception e) {
        log.error(e.getMessage());
        e.printStackTrace();
        System.out.println("发生未知异常"+e.getMessage());
//        BindingResult bindingResult = e.getBindingResult();
//
//        //只取第一个错误信息
//        String errorMesssage = "校验失败:" + bindingResult.getFieldErrors().get(0).getDefaultMessage();
//
//        return response;
        return null;
    }

}

