package com.neu.SP01.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.neu.SP01.dao.DietCycleDao;
import com.neu.SP01.dao.MealDao;
import com.neu.SP01.po.Meal;
import com.neu.SP01.po.PageResponseBean;
import com.neu.SP01.po.ResponseBean;

@Service
public class MealService {
	@Autowired
	private MealDao md;
	@Autowired
	private DietCycleDao dcd;
	
	public ResponseBean<Integer> addFood(Meal meal){
		try {
            // 参数校验
            if ( meal.getName() == null ||meal.getType()==null) {
                return new ResponseBean<>(500, "信息不能为空");
            }

            // 执行更新操作
            int affectedRows = md.addMeal(meal);

            if (affectedRows > 0) {
                return new ResponseBean<>(200,"添加成功",affectedRows);
            } else {
                return new ResponseBean<>(500, "添加失败");
            }
        } catch (Exception e) {
            // 记录错误日志
            e.printStackTrace();
            return new ResponseBean<>(500, "系统错误: " + e.getMessage());
        }
	}
	
	public PageResponseBean<List<Meal>> searchFood(String name, Integer type, long pageNum, long pageSize) {
        try {
            // 计算偏移量
            long offset = (pageNum - 1) * pageSize;
            
            // 执行分页查询（分页参数在DAO层处理）
            List<Meal> meals = md.findMealsByPage(name, type, offset, pageSize);
            
            // 获取总数
            long total = md.countMeals(name, type);
            
            // 创建响应对象
            PageResponseBean<List<Meal>> response = new PageResponseBean<>();
            if (meals == null || meals.isEmpty()) {
    	        response.setStatus(500);
    	        response.setMsg("无符合条件的数据");
    	        response.setData(null);
    	        response.setTotal(0);
    	    } else {
    	        response.setStatus(200);
    	        response.setMsg("查询成功");
    	        response.setData(meals);
    	        response.setTotal(total);
    	    }
            
            
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return new PageResponseBean<>(500, "系统错误: " + e.getMessage());
        }
    }
	
	public ResponseBean<Integer> editFood(Meal meal){
		try {
            // 参数校验
            if ( meal.getMealId() == null) {
                return new ResponseBean<>(500, "ID信息不能为空");
            }

            // 执行更新操作
            int affectedRows = md.updateMeal(meal);

            if (affectedRows > 0) {
            	
                return new ResponseBean<>(200,"修改成功",affectedRows);
            } else {
                return new ResponseBean<>(500, "修改失败");
            }
        } catch (Exception e) {
            // 记录错误日志
            e.printStackTrace();
            return new ResponseBean<>(500, "系统错误: " + e.getMessage());
        }
	}
	
	public ResponseBean<Integer> deleteFood(Integer mealId) {
	    try {
	        // 参数校验
	        if (mealId == null) {
	            return new ResponseBean<>(500, "ID不能为空");  // 400表示客户端错误
	        }
	        // 执行删除操作
	        int affectedRows = md.deleteMeal(mealId);
	        if (affectedRows == 0) {
	            return new ResponseBean<>(500, "删除失败");  // 404表示资源不存在
	        }
	        // 尝试更新关联的膳食周期（无论成功与否都不影响主操作）
	        try {
	            dcd.updateDietCycleByMealId(mealId);
	        } catch (Exception e) {
	        }

	        return new ResponseBean<>(200, "删除成功", affectedRows);
	        
	    } catch (Exception e) {
	        
	        return new ResponseBean<>(500, "系统错误: " + e.getMessage());
	    }
	}
    
}