package com.neu.SP01.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.neu.SP01.dao.NursingServiceDao;
import com.neu.SP01.po.NursingServiceDailyDTO;
import com.neu.SP01.po.PageResponseBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class) // 所有异常都触发回滚
public class NursingServiceService {
    @Autowired
    private NursingServiceDao nsd;
    
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
}