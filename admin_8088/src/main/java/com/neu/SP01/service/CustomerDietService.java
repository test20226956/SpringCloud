package com.neu.SP01.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.neu.SP01.dao.CustomerDietDao;
import com.neu.SP01.po.CustCheckInDietDTO;
import com.neu.SP01.po.CustomerDiet;
import com.neu.SP01.po.PageResponseBean;
import com.neu.SP01.po.ResponseBean;

@Service
public class CustomerDietService {
	
	@Autowired
	private CustomerDietDao cdd;
	/*查询显示*/
	public PageResponseBean<List<CustCheckInDietDTO>> searchCustDiet(String name, String checkInTime, 
	        long pageNum, long pageSize) {
	    // 计算偏移量
	    long offset = (pageNum - 1) * pageSize;
	    // 查询分页数据
	    List<CustCheckInDietDTO> list = cdd.searchCustomers(
	        name, 
	        checkInTime,
	        offset, 
	        pageSize
	    );
	    // 查询总数
	    long total = cdd.countSearchCustomers( 
	        name, 
	        checkInTime
	    );
	    // 构建分页响应
	    PageResponseBean<List<CustCheckInDietDTO>> response = new PageResponseBean<>();
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
	
	public ResponseBean<Integer> editCustDiet(CustomerDiet customerDiet) {
        try {
            // 参数校验
            if (customerDiet == null || customerDiet.getCustomerDietId() == null) {
                return new ResponseBean<>(500, "ID不能为空");
            }

            // 执行更新操作
            int affectedRows = cdd.editCustDiet(customerDiet);

            if (affectedRows > 0) {
                return new ResponseBean<>(200,"编辑成功",affectedRows);
            } else {
                return new ResponseBean<>(500, "编辑失败");
            }
        } catch (Exception e) {
            // 记录错误日志
            e.printStackTrace();
            return new ResponseBean<>(500, "系统错误: " + e.getMessage());
        }
    }
}