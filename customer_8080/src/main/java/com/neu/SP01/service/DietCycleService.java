package com.neu.SP01.service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.neu.SP01.dao.DietCycleDao;
import com.neu.SP01.po.DietCycle;
import com.neu.SP01.po.DietCycleDetail;
import com.neu.SP01.po.ResponseBean;

@Service
public class DietCycleService {
	@Autowired
	private DietCycleDao dcd;
	//添加
	public ResponseBean<Integer> addDiet(DietCycle diet){

		try {
            // 参数校验
            if ( diet == null ) {
                return new ResponseBean<>(500, "信息不能为空");
            }

            // 执行更新操作
            int affectedRows = dcd.addDietCycle(diet);

            if (affectedRows > 0) {
                return new ResponseBean<>(200,"添加成功",affectedRows);
            } else {
                return new ResponseBean<>(500, "添加失败");
            }
        } catch (Exception e) {
            // 记录错误日志
            e.printStackTrace();
            return new ResponseBean<>(500, "系统错误");
        }
	}
	//删除
	public ResponseBean<Integer> deleteDiet(Integer dietCycleId){

		try {
            // 参数校验
            if ( dietCycleId == null ) {
                return new ResponseBean<>(500, "ID不能为空");
            }

            // 执行更新操作
            int affectedRows = dcd.deleteDietCycle(dietCycleId);

            if (affectedRows > 0) {
                return new ResponseBean<>(200,"删除成功",affectedRows);
            } else {
                return new ResponseBean<>(500, "删除失败");
            }
        } catch (Exception e) {
            // 记录错误日志
            e.printStackTrace();
            return new ResponseBean<>(500, "系统错误");
        }
	}
	
	//修改
		public ResponseBean<Integer> editDiet(DietCycle diet){

			try {
	            // 参数校验
	            if ( diet == null||diet.getDietCycleId()==null ) {
	                return new ResponseBean<>(500, "信息不能为空");
	            }

	            // 执行更新操作
	            int affectedRows = dcd.updateDietCycle(diet);

	            if (affectedRows > 0) {
	                return new ResponseBean<>(200,"修改成功",affectedRows);
	            } else {
	                return new ResponseBean<>(500, "修改失败");
	            }
	        } catch (Exception e) {
	            // 记录错误日志
	            e.printStackTrace();
	            return new ResponseBean<>(500, "系统错误");
	        }
		}
		
		//展示
		public ResponseBean<List<DietCycleDetail>> getByDate(String date) {
	        try {
	            // 1. 参数校验
	            if (date == null || date.isEmpty()) {
	                return new ResponseBean<>(500, "日期参数不能为空");
	            }

	            // 3. 执行查询
	            List<DietCycleDetail> details = dcd.selectDietCycleWithMealByDate(date);
	            
	            // 4. 处理查询结果
	            if (details.isEmpty()) {
	                return new ResponseBean<>(500, "该日期没有膳食安排");
	            }
	            
	            return new ResponseBean<>(200,"查询成功",details);
	        } catch (Exception e) {
	            return new ResponseBean<>(500, "系统错误");
	        }
	    }
		
		//展示
				public ResponseBean<List<DietCycleDetail>> getByType(String date,Integer type) {
			        try {
			            // 1. 参数校验
			            if (date == null || date.isEmpty()||type == null) {
			                return new ResponseBean<>(500, "参数不能为空");
			            }

			            // 3. 执行查询
			            List<DietCycleDetail> details = dcd.selectDietCycleWithMealByType(date,type);
			            
			            // 4. 处理查询结果
			            if (details.isEmpty()) {
			                return new ResponseBean<>(500, "没有符合条件的结果");
			            }
			            
			            return new ResponseBean<>(200,"查询成功",details);
			        } catch (Exception e) {
			            return new ResponseBean<>(500, "系统错误");
			        }
			    }
    
}