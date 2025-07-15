package com.neu.SP01.service;

import java.time.LocalDate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.neu.SP01.dao.BedRecordDao;
import com.neu.SP01.dao.CheckInRecordDao;
import com.neu.SP01.dao.CustomerDao;
import com.neu.SP01.po.BedRecord;
import com.neu.SP01.po.CheckInRecord;
import com.neu.SP01.po.CustCheckInDTO;
import com.neu.SP01.po.CustCheckInNurseDTO;
import com.neu.SP01.po.CustDailyNursingDTO;
import com.neu.SP01.po.CustNursingManageDTO;
import com.neu.SP01.po.Customer;
import com.neu.SP01.po.PageResponseBean;
import com.neu.SP01.po.ResponseBean;

@Service
@Transactional(rollbackFor = Exception.class) // 添加此注解
public class CustomerService {
	@Autowired
    private CustomerDao cd;
	@Autowired
    private CheckInRecordDao cird;
    @Autowired
    private BedRecordDao brd;
    private static final Logger log = LoggerFactory.getLogger(CheckInRecordService.class);
    
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
	        log.error("编辑客户信息失败，客户ID: {}", data != null ? data.getCustomerId() : "null", e);
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