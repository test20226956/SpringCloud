package com.neu.SP01.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.neu.SP01.dao.NursingRecordDao;
import com.neu.SP01.dao.NursingServiceDao;
import com.neu.SP01.po.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.neu.SP01.dao.UserDao;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class NursingRecordService {
    @Autowired
    private NursingRecordDao nrd;
    @Autowired
    private NursingServiceDao nsd;
    //添加老人护理记录
    @Transactional
    public boolean addNursingRecord(NursingRecord nr){
        nrd.addNursingRecord(nr);// 插入护理记录
        nsd.updateNursingServiceAmount(nr.getCount(),nr.getNursingServiceId());//// 更新剩余服务次数
        return true;
    }
    //展示老人所有的护理记录（护工 健康管家 护理记录）
    public List<CustNursingRecordDTO> findByCustomerId(Integer customerId){
       /* //设置分页参数
        PageHelper.startPage(pageNum, pageSize);
        //执行查询
        List<CustNursingRecordDTO> cords = nrd.findByCustomerId(customerId);
        // 3. 获取分页信息
        Page<CustNursingRecordDTO> p =(Page<CustNursingRecordDTO>)cords;
        // 4. 构建响应对象
        PageResponseBean<List<CustNursingRecordDTO>> response = new PageResponseBean<>();
        if(p.getTotal()!=0){
            response.setStatus(200); // 成功状态码
            response.setMsg("查询成功"); // 成功消息
            response.setData(p.getResult()); // 当前页数据
            response.setTotal(p.getTotal()); // 总记录数
        }else{
            response.setStatus(500); // 成功状态码
            response.setMsg("无数据"); // 成功消息
            response.setData(p.getResult()); // 当前页数据
            response.setTotal(p.getTotal()); // 总记录数
        }
        return response;*/
        List<CustNursingRecordDTO> cords = nrd.findByCustomerId(customerId);
        return cords;
    }
    //搜索护理记录（原型中体现的名称和护理时间 动态sql）
    public List<CustNursingRecordDTO> findByNameAndTime(Integer customerId,String name, String time){
        List<CustNursingRecordDTO> byNameAndTime = nrd.findByNameAndTime(customerId, name, time);
        return byNameAndTime;
    }
    //逻辑删除老人的护理记录
    public boolean deleteByIds(List<Integer> ids){
        nrd.deleteByIds(ids);
        return true;
    }
    
    public PageResponseBean<List<NursingRecordDTO>> showNursingRecord(Integer customerId, String time,String projectName,long pageNum, long pageSize) {
        // 计算偏移量
        long offset = (long) (pageNum - 1) * pageSize;
        
        // 查询当前页数据
        List<NursingRecordDTO> records = nrd.showNursingRecord(
            customerId,time,projectName,offset, pageSize);
        
        // 查询总记录数
        long total = nrd.countSearchRecords(customerId,time,projectName);
     // 构建分页响应
	    PageResponseBean<List<NursingRecordDTO>> response = new PageResponseBean<>();
	 // 检查查询结果
	    if (records == null || records.isEmpty()) {
	        response.setStatus(500);
	        response.setMsg("无符合条件的数据");
	        response.setData(null);
	        response.setTotal(0);
	    } else {
	        response.setStatus(200);
	        response.setMsg("查询成功");
	        response.setData(records);
	        response.setTotal(total);
	    }
	    
	    return response;
        
    }
    
    public ResponseBean<Integer> deleteNursingRecord(Integer nursingRecordId) {
        
    	Integer res=nrd.deleteNursingRecord(nursingRecordId);
        
        
     // 构建分页响应
	    ResponseBean<Integer> response = new ResponseBean<>();
	 // 检查查询结果
	    if (res <=0) {
	        response.setStatus(500);
	        response.setMsg("删除失败");
	        response.setData(null);
	        
	    } else {
	        response.setStatus(200);
	        response.setMsg("删除成功");
	    }
	    
	    return response;
        
    }

    //客户端护理查看->展示老人对应护理项目下的护理记录
    public List<CustNursingRecordDTO> findByNursingServiceId(Integer nursingServiceId){
        List<CustNursingRecordDTO> byNursingServiceId = nrd.findByNursingServiceId(nursingServiceId);
        return byNursingServiceId;
    }
}