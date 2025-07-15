package com.neu.SP01.service;

import java.time.LocalDate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neu.SP01.po.*;
import com.neu.SP01.util.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.neu.SP01.dao.BedRecordDao;
import com.neu.SP01.dao.CheckInRecordDao;
import com.neu.SP01.dao.CustomerDao;

@Service
@Transactional(rollbackFor = Exception.class) // 添加此注解
public class CustomerService {
	@Autowired
    private CustomerDao cd;
	@Autowired
    private CheckInRecordDao cird;
    @Autowired
    private BedRecordDao brd;
   
    
	/*查自理*/
	public PageResponseBean<List<CustCheckInDTO>> getSelfCareCustomersByPage(long pageNum, long pageSize) {
        // 计算偏移量
        long offset = (pageNum - 1) * pageSize;
        
        // 查询分页数据
        List<CustCheckInDTO> list = cd.showSelfCust(offset, pageSize);
        
        // 查询总数
        long total = cd.countSelfCareCustomers();
        
        // 构建分页响应
        PageResponseBean<List<CustCheckInDTO>> response = new PageResponseBean<>();
        if (list == null || list.isEmpty()) {
	        response.setStatus(500);
	        response.setMsg("无符合条件的数据");
	        response.setData(null);
	        response.setTotal(0);
	    } else {
	        response.setStatus(200);
	        response.setMsg("查询成功");
	        response.setData(list);
	        response.setTotal(total);
	    }
        
        return response;
    }
	/*查护理*/
	public PageResponseBean<List<CustCheckInNurseDTO>> getCareCustomersByPage(long pageNum, long pageSize) {
        // 计算偏移量
        long offset = (pageNum - 1) * pageSize;
        
        // 查询分页数据
        List<CustCheckInNurseDTO> list = cd.showCareCust(offset, pageSize);
        
        // 查询总数
        long total = cd.countCareCustomers();
        
        // 构建分页响应
        PageResponseBean<List<CustCheckInNurseDTO>> response = new PageResponseBean<>();
        if (list == null || list.isEmpty()) {
	        response.setStatus(500);
	        response.setMsg("无符合条件的数据");
	        response.setData(null);
	        response.setTotal(0);
	    } else {
	        response.setStatus(200);
	        response.setMsg("查询成功");
	        response.setData(list);
	        response.setTotal(total);
	    }
        
        return response;
    }
	
	/*全查(在护理下）*/
	public PageResponseBean<List<CustCheckInNurseDTO>> searchCareCustomers(String name, String checkInTime, Integer nursingLevelId,
	        long pageNum, long pageSize) {
	    // 计算偏移量
	    long offset = (pageNum - 1) * pageSize;
	    
	    // 查询分页数据
	    List<CustCheckInNurseDTO> list = cd.searchCareCustomers(
	        name, 
	        checkInTime,
	        nursingLevelId,
	        offset, 
	        pageSize
	    );
	    
	    // 查询总数
	    long total = cd.countSearchCareCustomers( 
	        name, 
	        checkInTime,
	        nursingLevelId
	    );
	    
	    // 构建分页响应
	    PageResponseBean<List<CustCheckInNurseDTO>> response = new PageResponseBean<>();
	    
	    // 检查查询结果
	    if (list == null || list.isEmpty()) {
	        response.setStatus(500);
	        response.setMsg("无符合条件的数据");
	        response.setData(null);
	        response.setTotal(0);
	    } else {
	        response.setStatus(200);
	        response.setMsg("查询成功");
	        response.setData(list);
	        response.setTotal(total);
	    }
	    
	    return response;
	}
	
	/*全查(在无级别护理下）*/
	public PageResponseBean<List<CustCheckInNurseDTO>> searchNoLevelCareCustomers(String name, String checkInTime,
	        long pageNum, long pageSize) {
	    // 计算偏移量
	    long offset = (pageNum - 1) * pageSize;
	    
	    // 查询分页数据
	    List<CustCheckInNurseDTO> list = cd.searchNoLevelCareCustomers(
	        name, 
	        checkInTime,
	        offset, 
	        pageSize
	    );
	    
	    // 查询总数
	    long total = cd.countSearchNoLevelCareCustomers( 
	        name, 
	        checkInTime
	    );
	    
	    // 构建分页响应
	    PageResponseBean<List<CustCheckInNurseDTO>> response = new PageResponseBean<>();
	    
	    // 检查查询结果
	    if (list == null || list.isEmpty()) {
	        response.setStatus(500);
	        response.setMsg("无符合条件的数据");
	        response.setData(null);
	        response.setTotal(0);
	    } else {
	        response.setStatus(200);
	        response.setMsg("查询成功");
	        response.setData(list);
	        response.setTotal(total);
	    }
	    
	    return response;
	}
	
	/*全查*/
	public PageResponseBean<List<CustCheckInDTO>> searchCustomers(String selfCare, String name, String checkInTime, 
	        long pageNum, long pageSize) {
	    // 计算偏移量
	    long offset = (pageNum - 1) * pageSize;
	    
	    // 查询分页数据
	    List<CustCheckInDTO> list = cd.searchCustomers(
	        selfCare, 
	        name, 
	        checkInTime,
	        offset, 
	        pageSize
	    );
	    
	    // 查询总数
	    long total = cd.countSearchCustomers(
	        selfCare, 
	        name, 
	        checkInTime
	    );
	    
	    // 构建分页响应
	    PageResponseBean<List<CustCheckInDTO>> response = new PageResponseBean<>();
	    
	    // 检查查询结果
	    if (list == null || list.isEmpty()) {
	        response.setStatus(500);
	        response.setMsg("无符合条件的数据");
	        response.setData(null);
	        response.setTotal(0);
	    } else {
	        response.setStatus(200);
	        response.setMsg("查询成功");
	        response.setData(list);
	        response.setTotal(total);
	    }
	    
	    return response;
	}
	
	/*查以前的老人*/
	public ResponseBean<Customer> searchCustByIdentity(String identity) {
	    // 参数校验
	    if (identity == null || identity.trim().isEmpty()) {
	        return new ResponseBean<>(500, "身份证号不能为空", null);
	    }
	    
	    // 查询数据库
	    Customer customer = cd.searchCustByIdentity(identity);
	    
	    // 判断查询结果
	    if (customer == null) {
	        return new ResponseBean<>(500, "无历史记录", null);
	    }
	    
	    return new ResponseBean<>(200, "查询成功", customer);
	}
	
	//更新
	public ResponseBean<Integer> editCust(Customer data, String endTime) {
	    try {
	        // 1. 参数校验
	        if (data == null || data.getCustomerId() == null) {
	            return new ResponseBean<>(500, "客户ID不能为空", null);
	        }
	        
	        // 2. 更新客户基本信息
	        int updated = cd.updateCustomer(data);
	        if (updated == 0) {
	            return new ResponseBean<>(500, "未找到该客户", null);
	        }
	        
	        // 3. 处理合同到期时间（如果提供了endTime）
	        if (endTime != null && !endTime.isEmpty()) {
	            // 获取当前有效的入住记录
	            CheckInRecord activeRecord = cird.findActiveCheckInRecord(data.getCustomerId());
	            if (activeRecord != null) {
	                // 总是更新入住记录结束时间
	                cird.updateEndTime(activeRecord.getCheckInRecordId(), endTime);
	                
	                // 更新关联的床位记录（只有当新结束时间早于原结束时间时才更新）
	                BedRecord bedRecord = brd.findActiveBedRecord(activeRecord.getCheckInRecordId());
	                if (bedRecord != null && bedRecord.getEndTime() != null) {
	                    LocalDate originalBedEndDate = LocalDate.parse(bedRecord.getEndTime());
	                    LocalDate newEndDate = LocalDate.parse(endTime);
	                    
	                    if (newEndDate.isBefore(originalBedEndDate)) {
	                        brd.updateBedEndTime(bedRecord.getBedRecordId(), endTime);
	                    }
	                }
	            }
	        }
	        
	        return new ResponseBean<>(200, "更新成功", 1);
	        
	    } catch (Exception e) {
	       
	        return new ResponseBean<>(500, "更新失败: " + e.getMessage(), null);
	    }
	}
	
	/*展示没有护工的护理老人*/
	public PageResponseBean<List<CustCheckInNurseDTO>> showUnCust(long pageNum, long pageSize) {
        // 计算偏移量
        long offset = (pageNum - 1) * pageSize;
        
        // 查询分页数据
        List<CustCheckInNurseDTO> list = cd.showUnCust(offset, pageSize);
        
        // 查询总数
        long total = cd.countUnCustomers();
        
        // 构建分页响应
        PageResponseBean<List<CustCheckInNurseDTO>> response = new PageResponseBean<>();
        if (list == null || list.isEmpty()) {
	        response.setStatus(500);
	        response.setMsg("无符合条件的数据");
	        response.setData(null);
	        response.setTotal(0);
	    } else {
	        response.setStatus(200);
	        response.setMsg("查询成功");
	        response.setData(list);
	        response.setTotal(total);
	    }
        
        return response;
    }
	
	/*全查没有护工的(在护理下）*/
	public PageResponseBean<List<CustCheckInNurseDTO>> searchUnCust(String name, String checkInTime, Integer nursingLevelId,
	        long pageNum, long pageSize) {
	    // 计算偏移量
	    long offset = (pageNum - 1) * pageSize;
	    
	    // 查询分页数据
	    List<CustCheckInNurseDTO> list = cd.searchUnCust(
	        name, 
	        checkInTime,
	        nursingLevelId,
	        offset, 
	        pageSize
	    );
	    
	    // 查询总数
	    long total = cd.countSearchUnCust( 
	        name, 
	        checkInTime,
	        nursingLevelId
	    );
	    
	    // 构建分页响应
	    PageResponseBean<List<CustCheckInNurseDTO>> response = new PageResponseBean<>();
	    
	    // 检查查询结果
	    if (list == null || list.isEmpty()) {
	        response.setStatus(500);
	        response.setMsg("无符合条件的数据");
	        response.setData(null);
	        response.setTotal(0);
	    } else {
	        response.setStatus(200);
	        response.setMsg("查询成功");
	        response.setData(list);
	        response.setTotal(total);
	    }
	    
	    return response;
	}
	
	
	public ResponseBean<Integer> call(Integer customerId,String date){

		try {
            // 参数校验
            if ( customerId == null || date==null|| date.trim().isEmpty()) {
                return new ResponseBean<>(500, "信息不能为空");
            }

            // 执行更新操作
            int affectedRows = cd.call(customerId,date);

            if (affectedRows > 0) {
                return new ResponseBean<>(200,"呼叫成功",affectedRows);
            } else {
                return new ResponseBean<>(500, "呼叫失败");
            }
        } catch (Exception e) {
            // 记录错误日志
            e.printStackTrace();
            return new ResponseBean<>(500, "系统错误: " + e.getMessage());
        }
	}
	
	public ResponseBean<Integer> noCall(Integer callId){

		try {
            // 参数校验
            if ( callId == null ) {
                return new ResponseBean<>(500, "信息不能为空");
            }

            // 执行更新操作
            int affectedRows = cd.noCall(callId);

            if (affectedRows > 0) {
                return new ResponseBean<>(200,"回应成功",affectedRows);
            } else {
                return new ResponseBean<>(500, "回应失败");
            }
        } catch (Exception e) {
            // 记录错误日志
            e.printStackTrace();
            return new ResponseBean<>(500, "系统错误: " + e.getMessage());
        }
	}
	
	public ResponseBean<Map<String, Object>> listCall(Integer userId) {
	    try {
	        if (userId == null || userId <= 0) {
	            return new ResponseBean<>(500, "ID不能为空");
	        }

	        List<CustomerWithCall> customers = cd.findCustomerCallsByUserId(userId);
	        Integer count = cd.countCustomerCallsByUserId(userId);

	        Map<String, Object> result = new HashMap<>();
	        result.put("list", customers);
	        result.put("total", count);

	        if (customers != null && !customers.isEmpty()) {
	            return new ResponseBean<>(200, "查看成功", result);
	        } else {
	            return new ResponseBean<>(500, "无老人呼叫", result);
	        }
	    } catch (Exception e) {
	      
	        return new ResponseBean<>(500, "系统错误");
	    }
	}


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
	@Autowired
	RedisTemplate redisTemplate;
	//客户端老人登录
	public ResponseBean<Customer> login(String tel, String password) throws JsonProcessingException {
		// 1. 验证账号是否存在
		CustNursingManageDTO custByTel = cd.findCustByTel(tel);
		if (custByTel == null) {
			return new ResponseBean<>(500, "手机号不存在");
		}

		// 2. 验证密码是否正确
		String pwd = cd.findPwdByCustomerId(custByTel.getCustomerId());
		if (!pwd.equals(password)) {
			return new ResponseBean<>(500, "密码错误");
		}

		// 3. 登录成功，返回用户信息
		ObjectMapper objectMapper = new ObjectMapper();
		String s = objectMapper.writeValueAsString(custByTel);
		String jwt = JwtUtils.createToken(s);//jwt包含了当前登录的用户信息
		redisTemplate.opsForValue().set(custByTel.getCustomerId().toString(),jwt,20, TimeUnit.MINUTES);
		return new ResponseBeanJWT(200, "登录成功",custByTel,jwt);
	}

	//客户端显示老人详细信息
	public ResponseBean<ClientCustDTO> findCustById(Integer customerId){
		ClientCustDTO custById = cd.findCustById(customerId);
		return new ResponseBean<>(200,"查询成功",custById);
	}
	//修改老人头像
	public boolean updateImageById(Integer customerId,String image){
		cd.updateImageById(customerId,image);
		return true;
	}
	//修改老人手机号
	public boolean updateTelById(Integer customerId,String tel){
		cd.updateTelById(customerId,tel);
		return true;
	}
}