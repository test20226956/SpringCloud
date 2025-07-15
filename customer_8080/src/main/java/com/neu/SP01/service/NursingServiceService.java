package com.neu.SP01.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.neu.SP01.dao.CheckInRecordDao;
import com.neu.SP01.dao.NursingServiceDao;
import com.neu.SP01.po.CheckInRecord;
import com.neu.SP01.po.NursingService;
import com.neu.SP01.po.NursingServiceDTO;
import com.neu.SP01.po.NursingServiceDailyDTO;
import com.neu.SP01.po.PageResponseBean;
import com.neu.SP01.po.ResponseBean;

@Service
@Transactional(rollbackFor = Exception.class) // 所有异常都触发回滚
public class NursingServiceService {
    @Autowired
    private NursingServiceDao nsd;
    @Autowired
    private CheckInRecordDao cird;
    
    /*======对应原型护工 日常护理 显示用户的护理服务=====*/
    public PageResponseBean<List<NursingServiceDailyDTO>> findNursingServiceByCustomerId(Integer pageNum, Integer pageSize, Integer customerId){
        //设置分页参数
        PageHelper.startPage(pageNum, pageSize);
        //执行查询
        List<NursingServiceDailyDTO> cords = nsd.findNursingServiceByCustomerId(customerId);
        // 3. 获取分页信息
        Page<NursingServiceDailyDTO> p =(Page<NursingServiceDailyDTO>)cords;
        // 4. 构建响应对象
        PageResponseBean<List<NursingServiceDailyDTO>> response = new PageResponseBean<>();
        if(p.getTotal()!=0){
            response.setStatus(200); // 成功状态码
            response.setMsg("查询成功"); // 成功消息
            response.setData(p.getResult()); // 当前页数据
            response.setTotal(p.getTotal()); // 总记录数
        }else{
            response.setStatus(500); // 成功状态码
            response.setMsg("该老人暂无护理服务"); // 成功消息
            response.setData(p.getResult()); // 当前页数据
            response.setTotal(p.getTotal()); // 总记录数
        }
        return response;
    }
    //对应原型护工 日常护理  按项目名字搜索用户的持有的护理服务
    public PageResponseBean<List<NursingServiceDailyDTO>> findNursingServiceByName(Integer pageNum, Integer pageSize, Integer customerId,String name){
        //设置分页参数
        PageHelper.startPage(pageNum, pageSize);
        //执行查询
        List<NursingServiceDailyDTO> cords = nsd.findNursingServiceByName(customerId,name);
        // 3. 获取分页信息
        Page<NursingServiceDailyDTO> p =(Page<NursingServiceDailyDTO>)cords;
        // 4. 构建响应对象
        PageResponseBean<List<NursingServiceDailyDTO>> response = new PageResponseBean<>();
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
    
    //移除护理级别及服务
    public ResponseBean<Integer> deleteNursingLevel(Integer customerId) {
        try {
            // 1. 参数校验
            if (customerId == null) {
                return new ResponseBean<>(500, "客户ID不能为空");
            }

            // 2. 级联操作1：移除客户护理级别（设为NULL）
            int updatedRecords = cird.clearNursingLevelByCustomerId(customerId);

            // 3. 级联操作2：禁用该客户在当前级别的所有护理服务
            int disabledServices = nsd.deleteNursingService(customerId);


            return new ResponseBean<>(200, "移除成功", updatedRecords + disabledServices);

        } catch (Exception e) {
            return new ResponseBean<>(500, "移除失败: " + e.getMessage());
        }
    }
    //批量添加
    @Transactional
    public ResponseBean<Integer> batchAddNursingServices(List<NursingService> nursingServices) {
        try {
            if (nursingServices == null || nursingServices.isEmpty()) {
                return new ResponseBean<>(500, "护理服务列表不能为空");
            }

            int affectedRows = nsd.batchInsertNursingServices(nursingServices);
            
            if (affectedRows > 0) {
                return new ResponseBean<>(200,"添加成功",affectedRows);
            } else {
                return new ResponseBean<>(500, "添加失败");
            }
        } catch (Exception e) {
            throw new RuntimeException("系统错误: " + e.getMessage(), e);
        }
    }
    //显示
    public ResponseBean<List<NursingServiceDTO>> showNursingPro(Integer customerId) {
        try {
            if (customerId == null || customerId <= 0) {
                return new ResponseBean<>(500, "客户ID不能为空");
            }
         // 1. 先检查并更新过期的护理服务
            updateExpiredServices();
            
            // 2. 查询客户护理服务
            List<NursingServiceDTO> result = nsd.findNursingServiceProjectByCustomerId(customerId);
            
            if (result == null || result.isEmpty()) {
                return new ResponseBean<>(500, "查询失败");
            }
            
            return new ResponseBean<>(200,"查询成功",result);
        } catch (Exception e) {
            return new ResponseBean<>(500, "系统错误: " + e.getMessage());
        }
    }
 // 更新过期护理服务状态
    private void updateExpiredServices() {
        // 获取当前日期（格式与数据库中的end_time一致）
        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        
        // 执行更新：end_time小于当天且state=1的记录改为state=-1
        nsd.updateExpiredServices(currentDate);
    }
    //续费
    public ResponseBean<Integer> editNursingPro(Integer nursingServiceId,Integer amount,String endTime) {
        try {
        	if (nursingServiceId == null || nursingServiceId <= 0) {
                return new ResponseBean<>(500, "服务ID不能为空");
            }
            
            Integer result = nsd.updateNursingServiceRenewal(nursingServiceId,amount,endTime);
            
            if (result == 0 ) {
                return new ResponseBean<>(500, "续费失败");
            }
            
            return new ResponseBean<>(200,"续费成功",result);
        } catch (Exception e) {
            return new ResponseBean<>(500, "系统错误: " + e.getMessage());
        }
    }
    
  //删除
    public ResponseBean<Integer> deleteNursingPro(Integer nursingServiceId) {
        try {
        	if (nursingServiceId == null || nursingServiceId <= 0) {
                return new ResponseBean<>(500, "服务ID不能为空");
            }
            
            Integer result = nsd.deleteNursingServiceById(nursingServiceId);
            
            if (result == 0 ) {
                return new ResponseBean<>(500, "移除失败");
            }
            
            return new ResponseBean<>(200,"移除成功",result);
        } catch (Exception e) {
            return new ResponseBean<>(500, "系统错误: " + e.getMessage());
        }
    }
    
    //添加
    public ResponseBean<Integer> addNursingProject(NursingService projectService) {
    try {
        // 1. 参数校验
        if (projectService.getCustomerId() == null || projectService.getNursingProjectId() == null) {
            return new ResponseBean<>(500, "客户ID和项目ID不能为空");
        }
        
       
        projectService.setNursingLevelId(projectService.getNursingLevelId());
        
        // 3. 设置默认值
        if (projectService.getAmount() == null) {
            projectService.setAmount(1); // 默认数量为1
        }
        if (projectService.getPurchaseTime() == null) {
            projectService.setPurchaseTime(LocalDate.now().toString()); // 默认当前日期
        }
        
        // 4. 检查并处理已有无效记录
        int inactiveCount = nsd.countInactiveService(
            projectService.getCustomerId(), 
            projectService.getNursingProjectId());
            
        if (inactiveCount > 0) {
            nsd.reactivateOldService(
                projectService.getCustomerId(), 
                projectService.getNursingProjectId());
        }
        
        // 5. 添加新记录
        int result = nsd.insertNewService(projectService);
        
        if (result > 0) {
            return new ResponseBean<>(200, "添加成功", projectService.getNursingServiceId());
        } else {
            return new ResponseBean<>(500, "添加失败");
        }
    } catch (Exception e) {
        return new ResponseBean<>(500, "系统错误: " + e.getMessage());
    }
}
  /*===============客户端护理查看===============*/
  public List<NursingServiceDailyDTO> findNursingServiceByCustomerIdOnClient(Integer customerId){
      //执行查询
      List<NursingServiceDailyDTO> cords = nsd.findNursingServiceByCustomerId(customerId);
      return cords;
  }
  public List<NursingServiceDailyDTO> findNursingServiceByNameOnClient(Integer customerId,String name){
        //执行查询
      List<NursingServiceDailyDTO> cords = nsd.findNursingServiceByName(customerId,name);
      return cords;
  }
}