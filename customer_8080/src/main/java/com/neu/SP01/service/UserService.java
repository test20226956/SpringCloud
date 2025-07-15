package com.neu.SP01.service;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neu.SP01.util.JwtUtils;
import com.neu.SP01.po.ResponseBeanJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.neu.SP01.dao.UserDao;
import com.neu.SP01.po.PageResponseBean;
import com.neu.SP01.po.ResponseBean;
import com.neu.SP01.po.User;

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
    
    public PageResponseBean<List<User>> getRegularUsers(int pageNum, int pageSize) {
        try {
            // 参数校验
            if (pageNum < 1) pageNum = 1;
            if (pageSize < 1 || pageSize > 100) pageSize = 10;
            
            // 计算偏移量
            int offset = (pageNum - 1) * pageSize;
            
            // 查询数据
            List<User> users = ud.findUsersByTypeWithPage(offset, pageSize);
            
            // 检查查询结果是否为空
            if (users == null || users.isEmpty()) {
                return new PageResponseBean<>(500, "无符合条件的数据", null);
            }
            
            // 查询总数
            long total = ud.countUsersByType();
            
            // 构建响应
            PageResponseBean<List<User>> response = new PageResponseBean<>(200, "查询成功", users);
            response.setTotal(total);
            return response;
            
        } catch (Exception e) {
            return new PageResponseBean<>(500, "系统错误：" + e.getMessage(), null);
        }
    }
    
    //查询
    public PageResponseBean<List<User>> searchRegularUsers(
            String userName, 
            String account, 
            String employmentDate,
            int pageNum, 
            int pageSize) {
        
        try {
            // 参数校验与默认值
            pageNum = Math.max(pageNum, 1);
            pageSize = Math.min(Math.max(pageSize, 1), 100);
            
           
            
            // 计算偏移量
            int offset = (pageNum - 1) * pageSize;
            
            // 查询数据
            List<User> users = ud.findUsersByConditions(
                    userName, account, employmentDate, offset, pageSize);
            
            // 检查结果
            if (users.isEmpty()) {
                return new PageResponseBean<>(500, "无符合条件的数据", null);
            }
            
            // 查询总数
            long total = ud.countUsersByConditions(
                    userName, account, employmentDate);
            
            // 构建响应
            PageResponseBean<List<User>> response = 
                new PageResponseBean<>(200, "查询成功", users);
            response.setTotal(total);
            return response;
            
        } catch (Exception e) {
            return new PageResponseBean<>(500, "系统错误：" + e.getMessage(), null);
        }
    }
    
    
    public ResponseBean<String> addUser(User user) {
        try {
            // 1. 参数校验
            if (user.getAccount() == null || user.getAccount().trim().isEmpty()) {
                return new ResponseBean<>(500, "账号不能为空", null);
            }
            if (user.getUserName() == null || user.getUserName().trim().isEmpty()) {
                return new ResponseBean<>(500, "用户名不能为空", null);
            }
            if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
                return new ResponseBean<>(500, "密码不能为空", null);
            }
            if (user.getType() == null) {
                return new ResponseBean<>(500, "用户类型不能为空", null);
            }

            // 2. 检查账号是否已存在
            if (ud.existsByAccount(user.getAccount())) {
                return new ResponseBean<>(500, "账号已存在", null);
            }

            user.setState(1);
            // 5. 保存用户
            int result = ud.insertUser(user);
            if (result > 0) {
                return new ResponseBean<>(200, "添加成功", null);
            } else {
                return new ResponseBean<>(500, "添加失败", null);
            }
        } catch (Exception e) {
         
            return new ResponseBean<>(500, "系统错误：" + e.getMessage(), null);
        }
    }
    
    
    public ResponseBean<String> updateUser(User user) {
        try {
            // 1. 参数校验
            if (user.getUserId() == null) {
                return new ResponseBean<>(500, "用户ID不能为空", null);
            }
            
            // 2. 检查用户是否存在
            User existingUser = ud.findById(user.getUserId());
            if (existingUser == null) {
                return new ResponseBean<>(500, "用户不存在", null);
            }
            
            
            // 4. 处理空字段（保留原值）
            if (user.getUserName() == null) {
                user.setUserName(existingUser.getUserName());
            }
            if (user.getPassword() == null) {
                user.setPassword(existingUser.getPassword());
            } else {
                // 实际项目中应对新密码加密
                // user.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            if (user.getTel() == null) {
                user.setTel(existingUser.getTel());
            }
            if (user.getEmail() == null) {
                user.setEmail(existingUser.getEmail());
            }
            if (user.getEmploymentDate() == null) {
                user.setEmploymentDate(existingUser.getEmploymentDate());
            }
            if (user.getAccount() == null) {
                user.setAccount(existingUser.getAccount());
            }
            if (user.getType() == null) {
                user.setType(existingUser.getType());
            }
            if (user.getState() == null) {
                user.setState(existingUser.getState());
            }
            
            // 5. 执行更新
            int result = ud.updateUser(user);
            if (result > 0) {
                return new ResponseBean<>(200, "更新成功", null);
            } else {
                return new ResponseBean<>(500, "更新失败", null);
            }
            
        } catch (Exception e) {
            return new ResponseBean<>(500, "系统错误：" + e.getMessage(), null);
        }
    }
    
    
    public ResponseBean<String> deleteUser(Integer userId) {
        try {
            // 1. 参数校验
            if (userId == null || userId <= 0) {
                return new ResponseBean<>(500, "用户ID不合法", null);
            }
            
            // 2. 检查用户是否存在且未删除
            if (!ud.existsActiveUser(userId)) {
                return new ResponseBean<>(500, "用户不存在或已被删除", null);
            }
            
            // 3. 执行逻辑删除
            int result = ud.logicalDelete(userId);
            int res=ud.logicalDeleteUser(userId);
            if (result > 0) {
                return new ResponseBean<>(200, "删除成功", null);
            } else {
                return new ResponseBean<>(500, "删除失败", null);
            }
            
        } catch (Exception e) {
            return new ResponseBean<>(500, "系统错误：" + e.getMessage(), null);
        }
    }
}