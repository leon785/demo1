package com.example.demo1.pojo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public class UserDto {

    private Integer userId;

    //    @JsonProperty("userName")
    @NotBlank(message = "用户名不为空")
    private String userName;
    @NotBlank(message = "密码不为空")
    @Length(min = 6, max = 12)
    private String password;
    @NotBlank(message = "邮箱格式不正确")
    private String email;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
