package com.example.demo1.controller;

import com.example.demo1.pojo.ResponseMessage;
import com.example.demo1.pojo.User;
import com.example.demo1.pojo.dto.UserDto;
import com.example.demo1.service.IUserService;
import com.example.demo1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController  // 接口方法返回对象，转换成json文本
@RequestMapping("/user")
public class UserController {

    @Autowired
    IUserService userService;

    // 增加
    @PostMapping
    public ResponseMessage<User> add(@Validated @RequestBody UserDto user) {
        User userNew = userService.add(user);
        return ResponseMessage.success(userNew);
    }

    // 查询
    @GetMapping("/{user_id}")
    public ResponseMessage<User> lookUp(@PathVariable("user_id") Integer userId) {
        User userNew = userService.getUser(userId);
        return ResponseMessage.success(userNew);
    }

    // 修改
    @PutMapping
    public ResponseMessage<User> edit(@Validated @RequestBody UserDto user) {
        User userNew = userService.edit(user);
        return ResponseMessage.success(userNew);
    }

    // 删除
    @DeleteMapping("/{user_id}")
    public ResponseMessage<User> delete(@PathVariable("user_id") Integer userId) {
        userService.delete(userId);
        return ResponseMessage.success();
    }
}
