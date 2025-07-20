package com.example.demo1.service;


import com.example.demo1.dao.UserDao;
import com.example.demo1.pojo.User;
import com.example.demo1.pojo.dto.UserDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService{

    @Autowired
    UserDao userDao;

    @Override
    public User add(UserDto user) {
        User userPojo = new User();
        BeanUtils.copyProperties(user, userPojo);
        return userDao.save(userPojo);
    }

    @Override
    public User getUser(Integer userId) {
        return userDao.findById(userId).orElseThrow(() -> {
            throw new IllegalArgumentException(("用户不存在，参数异常"));
        });
    }

    @Override
    public User edit(UserDto user) {
        User userPojo = new User();
        BeanUtils.copyProperties(user, userPojo);
        return userDao.save(userPojo);
    }

    @Override
    public void delete(Integer userId) {
        userDao.deleteById(userId);
    }


}
