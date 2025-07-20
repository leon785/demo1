package com.example.demo1.exception;

import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.example.demo1.pojo.ResponseMessage;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice  // 捕捉全局输出的异常
public class GlobalExceptionHandlerAdvice {

    // log
    Logger log = LoggerFactory.getLogger(GlobalExceptionHandlerAdvice.class);

    @ExceptionHandler({Exception.class})  // 什么异常的同一处理
    public ResponseMessage handlerException(Exception e, HttpServletRequest request, HttpServletResponse response) {
        log.error("统一异常：", e);
        return new ResponseMessage(500, "error", null);
    }
}
