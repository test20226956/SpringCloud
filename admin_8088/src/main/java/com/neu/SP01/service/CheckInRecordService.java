package com.neu.SP01.service;



import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.neu.SP01.dao.BedDao;
import com.neu.SP01.dao.BedRecordDao;
import com.neu.SP01.dao.CheckInRecordDao;
import com.neu.SP01.dao.CustomerDao;
import com.neu.SP01.dao.CustomerDietDao;
import com.neu.SP01.dao.FamilyDao;
import com.neu.SP01.dao.NursingRecordDao;
import com.neu.SP01.dao.NursingServiceDao;
import com.neu.SP01.dao.RoomDao;
import com.neu.SP01.po.Bed;
import com.neu.SP01.po.BedRecord;
import com.neu.SP01.po.CustCheckInDTO;
import com.neu.SP01.po.Customer;
import com.neu.SP01.po.ResponseBean;
import com.neu.SP01.po.Room;

@Service
@Transactional(rollbackFor = Exception.class) // 添加此注解
public class CheckInRecordService {
	
	@Autowired
    private CustomerDao cd;
	@Autowired
    private BedDao bd;
	@Autowired
    private BedRecordDao brd;
	@Autowired
    private FamilyDao fd;
	@Autowired
    private CheckInRecordDao cird;
	@Autowired
    private RoomDao rd;
	@Autowired
    private CustomerDietDao cdd;
	@Autowired
    private NursingRecordDao nrd;
	@Autowired
    private NursingServiceDao nsd;
	
	private static final Logger log = LoggerFactory.getLogger(CheckInRecordService.class);
	
	//添加
	public ResponseBean<String> addCustCheckIn(CustCheckInDTO data) {
        try {
            // 1. 参数校验
            if (data == null || data.getCustomer() == null || 
                data.getCheckInRecord() == null || data.getRoom() == null || 
                data.getBed() == null) {
            	TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); 
                return new ResponseBean<>(500, "参数不完整", null);
            }
            
         // 2. 检查老人是否已存在
            Customer existingCustomer = null;
            if (data.getCustomer() != null && data.getCustomer().getIdentity() != null) {
                existingCustomer = cd.searchCustByIdentity(data.getCustomer().getIdentity());
                
                // 检查是否已入住
                if (existingCustomer != null && cird.findByCustomerId(existingCustomer.getCustomerId()) != null) {
                	TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); 
                    return new ResponseBean<>(500, "该老人已入住", null);
                }
            }
            
            // 3. 处理老人信息
            Integer customerId;
            if (existingCustomer != null) {
                // 使用已存在的老人信息
                customerId = existingCustomer.getCustomerId();
                // 可选：更新老人信息（如果需要）
                data.getCustomer().setCustomerId(customerId);
                cd.updateCustomer(data.getCustomer());
            } else {
                // 添加新老人信息
                if (data.getCustomer() == null) {
                	TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); 
                    return new ResponseBean<>(500, "需要提供老人信息", null);
                }
                cd.insertCustomer(data.getCustomer());
                customerId = data.getCustomer().getCustomerId();
                if (customerId == null) {
                	TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); 
                    throw new RuntimeException("添加老人信息失败");
                }
            }
            
            // 2. 验证并获取完整的房间信息
            Room roomInfo = rd.findRoomByFloorAndNumber(
                data.getRoom().getFloor(), 
                data.getRoom().getRoomNumber());
            
            if (roomInfo == null) {
            	TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); 
                return new ResponseBean<>(500, "未找到指定的房间", null);
            }
            
            // 3. 验证并获取完整的床位信息
            Bed bedInfo = bd.findBedByRoomAndNumber(
                roomInfo.getRoomId(), 
                data.getBed().getBedNumber());
            
            if (bedInfo == null) {
            	TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); 
                return new ResponseBean<>(500, "未找到指定的床位", null);
            }
            
            if (bedInfo.getAvailable() != 0) {
            	TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); 
                return new ResponseBean<>(500, "该床位已被占用", null);
            }

            // 5. 添加家属信息（如果有）
            if (data.getFamily() != null) {
                data.getFamily().setCustomerId(customerId);
                fd.insertFamily(data.getFamily());
            }
            
            // 6. 设置入住记录
            data.getCheckInRecord().setCustomerId(customerId);
            data.getCheckInRecord().setState(1);
            
            // 处理日期字段
            if (data.getCheckInRecord().getCheckInTime() == null || 
                data.getCheckInRecord().getCheckInTime().isEmpty()) {
                data.getCheckInRecord().setCheckInTime(LocalDate.now().toString());
            }
            if (data.getCheckInRecord().getEndTime() == null || 
                    data.getCheckInRecord().getEndTime().isEmpty()) {
                    data.getCheckInRecord().setEndTime(LocalDate.now().toString());
                }
            // 7. 添加入住记录
            cird.insertCheckInRecord(data.getCheckInRecord());
            Integer checkInRecordId = data.getCheckInRecord().getCheckInRecordId();
            if (checkInRecordId == null) {
            	TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); 
                throw new RuntimeException("添加入住记录失败");
            }
            
            // 8. 设置床位记录
            BedRecord bedRecord = data.getBedRecord();
            bedRecord.setBedId(bedInfo.getBedId());
            bedRecord.setCheckInId(checkInRecordId);
            bedRecord.setState(1);
            if (bedRecord.getStartTime() == null || bedRecord.getStartTime().isEmpty()) {
                bedRecord.setStartTime(LocalDate.now().toString());
            }
            bedRecord.setStartTime(data.getCheckInRecord().getCheckInTime());
            bedRecord.setEndTime(data.getCheckInRecord().getEndTime());
            // 9. 添加床位记录
            brd.insertBedRecord(bedRecord);
            
            // 10. 更新床位状态为已占用
            bd.updateBedStatus(bedInfo.getBedId());
            //11.添加一条客户膳食
            cdd.addCustomerDiet(customerId);
            
            return new ResponseBean<>(200, "添加成功", null);
            
        } catch (Exception e) {
            log.error("添加老人入住信息失败，已回滚所有操作", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); 
            return new ResponseBean<>(500, "添加失败: " + e.getMessage(), null);
        }
    }
	
	
	public ResponseBean<String> deleteCustomer(Integer customerId) {
        try {
            // 1. 参数校验
            if (customerId == null || customerId <= 0) {
                return new ResponseBean<>(500, "客户ID不合法", null);
            }
            
            // 2. 查找当前显示的床位记录
            BedRecord activeBedRecord = brd.findActiveBedRecordByCustomer(customerId);
            if (activeBedRecord != null) {
                // 3. 更新床位状态为空闲
                bd.setBedAvailable(activeBedRecord.getBedId());
                
                // 4. 隐藏床位记录
                brd.hideBedRecord(activeBedRecord.getBedRecordId());
            }
            
         // 删除膳食配置 - 如果没有记录也不报错
            try {
                cdd.deleteCustDiet(customerId);
            } catch (Exception e) {
                log.warn("删除客户ID[{}]的膳食配置时出现警告: {}", customerId, e.getMessage());
            }
            
            // 删除护理记录 - 如果没有记录也不报错
            try {
                nrd.deleteOutNursingRecord(customerId);
            } catch (Exception e) {
                log.warn("删除客户ID[{}]的护理记录时出现警告: {}", customerId, e.getMessage());
            }
            //删除家人
            try {
                fd.deleteFamily(customerId);
            } catch (Exception e) {
                log.warn("删除客户ID[{}]的护理记录时出现警告: {}", customerId, e.getMessage());
            }
            
            // 删除护理服务 - 如果没有记录也不报错
            try {
                nsd.detectService(customerId);
            } catch (Exception e) {
                log.warn("删除客户ID[{}]的护理服务时出现警告: {}", customerId, e.getMessage());
            }
            
            // 5. 隐藏入住记录
            int updated=cird.hideCheckInRecord(customerId);
            
            
           
            if (updated == 0) {
            	TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); 
                return new ResponseBean<>(500, "未找到该客户", null);
            }
            
            log.info("成功逻辑删除客户ID: {}", customerId);
            return new ResponseBean<>(200, "删除成功", null);
            
        } catch (Exception e) {
            log.error("删除客户信息失败，客户ID: {}", customerId, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); 
            return new ResponseBean<>(500, "删除失败: " + e.getMessage(), null);
        }
    }
	//添加护工
	public ResponseBean<Integer> addUserCust(Integer customerId,Integer userId){
		try {
	        // 1. 参数校验
	        if (customerId == null) {
	            return new ResponseBean<>(500, "客户ID不能为空", null);
	        }
	        int res=cird.addUserCust(customerId, userId);
	        if (res == 0) {
	            return new ResponseBean<>(500, "添加失败", null);
	        }
	        return new ResponseBean<>(200, "添加成功", null);  
	    } catch (Exception e) {   
	        return new ResponseBean<>(500, "系统错误: " + e.getMessage(), null);
	    }
		
	}
	
	//给老人移除护工
		public ResponseBean<Integer> deleteUserCust(Integer customerId,Integer userId){
			try {
		        // 1. 参数校验
		        if (customerId == null) {
		            return new ResponseBean<>(500, "客户ID不能为空", null);
		        }
		        int res=cird.deleteUserCust(customerId, userId);
		        if (res == 0) {
		            return new ResponseBean<>(500, "移除失败", null);
		        }
		        return new ResponseBean<>(200, "移除成功", null);  
		    } catch (Exception e) {   
		        return new ResponseBean<>(500, "系统错误: " + e.getMessage(), null);
		    }
			
		}

}
