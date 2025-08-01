package com.example.demo1.pojo;

import org.springframework.http.HttpStatus;

public class ResponseMessage <T>{
    private Integer code;
    private String message;
    private T data;

    public ResponseMessage(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    // 成功有返回
    public static<T> ResponseMessage<T> success(T data) {
        return new ResponseMessage(HttpStatus.OK.value(), "success", data);
    }
    // 成功无返回
    public static<T> ResponseMessage<T> success() {
        return new ResponseMessage(HttpStatus.OK.value(), "success", null);
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
