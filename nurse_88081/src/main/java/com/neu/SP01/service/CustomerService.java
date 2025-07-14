package com.neu.SP01.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.neu.SP01.dao.CustomerDao;
import com.neu.SP01.po.CustDailyNursingDTO;
import com.neu.SP01.po.CustNursingManageDTO;
import com.neu.SP01.po.PageResponseBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Transactional(rollbackFor = Exception.class) // 添加此注解
public class CustomerService {
	@Autowired
    private CustomerDao cd;
	@Autowired
	RedisTemplate redisTemplate;

	/*==护工模块 显示该护工下的老人 对应请求路径"/user/showUserCust"==*/
	public PageResponseBean<List<CustDailyNursingDTO>> findUserCust(Integer pageNum, Integer pageSize, Integer userId){
		//设置分页参数
		PageHelper.startPage(pageNum, pageSize);
		//执行查询
		List<CustDailyNursingDTO> cdnds = cd.findUserCust(userId);
		// 3. 获取分页信息
		Page<CustDailyNursingDTO> p =(Page<CustDailyNursingDTO>)cdnds;
		// 4. 构建响应对象
		PageResponseBean<List<CustDailyNursingDTO>> response = new PageResponseBean<>();
		if(p.getTotal()!=0){
			response.setStatus(200); // 成功状态码
			response.setMsg("查询成功"); // 成功消息
			response.setData(p.getResult()); // 当前页数据
			response.setTotal(p.getTotal()); // 总记录数
		}else{
			response.setStatus(500); // 成功状态码
			response.setMsg("无客户，请联系管理员设置服务对象"); // 成功消息
			response.setData(p.getResult()); // 当前页数据
			response.setTotal(p.getTotal()); // 总记录数
		}
		return response;
	}
	//根据老人姓名模糊搜索
	public PageResponseBean<List<CustDailyNursingDTO>> findUserCustByName(Integer pageNum, Integer pageSize, Integer userId,String name){
		//设置分页参数
		PageHelper.startPage(pageNum, pageSize);
		//执行查询
		List<CustDailyNursingDTO> cdnds = cd.findUserCustByName(userId,name);
		// 3. 获取分页信息
		Page<CustDailyNursingDTO> p =(Page<CustDailyNursingDTO>)cdnds;
		// 4. 构建响应对象
		PageResponseBean<List<CustDailyNursingDTO>> response = new PageResponseBean<>();
		if(p.getTotal()!=0){
			response.setStatus(200); // 成功状态码
			response.setMsg("查询成功"); // 成功消息
			response.setData(p.getResult()); // 当前页数据
			response.setTotal(p.getTotal()); // 总记录数
		}else{
			response.setStatus(500); // 成功状态码
			response.setMsg("无符合条件的数据"); // 成功消息
			response.setData(p.getResult()); // 当前页数据
			response.setTotal(p.getTotal()); // 总记录数
		}
		return response;
	}
	//客户管理模块显示老人信息（其实是增加了身份证号，入住时间以及到期时间这三项内容）
	public PageResponseBean<List<CustNursingManageDTO>> findUserCustManage(Integer pageNum, Integer pageSize, Integer userId){
		//设置分页参数
		PageHelper.startPage(pageNum, pageSize);
		//执行查询
		List<CustNursingManageDTO> cdnds = cd.findUserCustManage(userId);
		// 3. 获取分页信息
		Page<CustNursingManageDTO> p =(Page<CustNursingManageDTO>)cdnds;
		// 4. 构建响应对象
		PageResponseBean<List<CustNursingManageDTO>> response = new PageResponseBean<>();
		if(p.getTotal()!=0){
			response.setStatus(200); // 成功状态码
			response.setMsg("查询成功"); // 成功消息
			response.setData(p.getResult()); // 当前页数据
			response.setTotal(p.getTotal()); // 总记录数
		}else{
			response.setStatus(500); // 成功状态码
			response.setMsg("无客户，请联系管理员设置服务对象"); // 成功消息
			response.setData(p.getResult()); // 当前页数据
			response.setTotal(p.getTotal()); // 总记录数
		}
		return response;
	}
	//客户管理模块根据老人姓名模糊搜索
	public PageResponseBean<List<CustNursingManageDTO>> findUserCustManageByName(Integer pageNum, Integer pageSize, Integer userId,String name){
		//设置分页参数
		PageHelper.startPage(pageNum, pageSize);
		//执行查询
		List<CustNursingManageDTO> cdnds = cd.findUserCustManageByName(userId,name);
		// 3. 获取分页信息
		Page<CustNursingManageDTO> p =(Page<CustNursingManageDTO>)cdnds;
		// 4. 构建响应对象
		PageResponseBean<List<CustNursingManageDTO>> response = new PageResponseBean<>();
		if(p.getTotal()!=0){
			response.setStatus(200); // 成功状态码
			response.setMsg("查询成功"); // 成功消息
			response.setData(p.getResult()); // 当前页数据
			response.setTotal(p.getTotal()); // 总记录数
		}else{
			response.setStatus(500); // 成功状态码
			response.setMsg("无符合条件的数据"); // 成功消息
			response.setData(p.getResult()); // 当前页数据
			response.setTotal(p.getTotal()); // 总记录数
		}
		return response;
	}
}