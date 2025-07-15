package com.neu.SP01.service;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neu.SP01.dao.UserDao;
import com.neu.SP01.po.ResponseBean;
import com.neu.SP01.po.ResponseBeanJWT;
import com.neu.SP01.po.User;
import com.neu.SP01.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {
    
    @Autowired
    private UserDao ud;
    @Autowired
    RedisTemplate redisTemplate;
    
    /*用户登录验证*/
    public ResponseBean<User> login(String account, String password) throws JsonProcessingException {
        // 1. 验证账号是否存在
        User user = ud.findUserByAccount(account);
        if (user == null) {
            return new ResponseBean<>(500, "账号不存在");
        }
        
        // 2. 验证密码是否正确
        if (!user.getPassword().equals(password)) {
            return new ResponseBean<>(500, "密码错误");
        }

        // 3. 登录成功，返回用户信息(密码置空)
        user.setPassword(null);
        ObjectMapper objectMapper = new ObjectMapper();
        String s = objectMapper.writeValueAsString(user);
        String jwt = JwtUtils.createToken(s);//jwt包含了当前登录的用户信息
        redisTemplate.opsForValue().set(user.getUserId().toString(),jwt,20, TimeUnit.MINUTES);
        return new ResponseBeanJWT(200, "登录成功",user,jwt);
    }
}