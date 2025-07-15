package com.neu.SP01.service;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.neu.SP01.dao.OrderDao;
import com.neu.SP01.po.DailyMealOrderStatus;
import com.neu.SP01.po.Order;
import com.neu.SP01.po.OrderDetail;
import com.neu.SP01.po.ResponseBean;

@Service

@Transactional(rollbackFor = Exception.class) // 添加事务注解，任何异常都会回滚
public class OrderService {
	@Autowired
	private OrderDao od;

	@Transactional
	public ResponseBean<String> createMealOrder(Integer customerId, Integer mealType, String date, String request,
			List<OrderDetail> orderDetails) {
		try {
			// 参数校验
			if (customerId == null || mealType == null || date == null || orderDetails == null) {
                return new ResponseBean<>(500, "必要参数不能为空");
            }


			// 检查是否已存在当日同类型订单
			int existingCount = od.countExistingOrder(customerId, mealType, date);
			if (existingCount > 0) {
				return new ResponseBean<>(500, "该餐次已点过餐");
			}

			// 创建订单主表
			Order order = new Order();
			order.setCustomerId(customerId);
			order.setType(mealType);
			order.setDate(date);
			order.setRequest(request);

			int orderResult = od.insertOrder(order);
			if (orderResult <= 0 || order.getOrderId() == null) {
				return new ResponseBean<>(500, "创建订单失败");
			}

			// 创建订单详情
			for (OrderDetail detail : orderDetails) {
				detail.setOrderId(order.getOrderId());
				int detailResult = od.insertOrderDetail(detail);
				if (detailResult <= 0) {
					throw new RuntimeException("添加订单详情失败");
				}
			}

			return new ResponseBean<>(200, "点餐成功", null);
		} catch (Exception e) {
			return new ResponseBean<>(500, "点餐失败" );
		}
	}
	//删除
	@Transactional
	public ResponseBean<Integer> deleteMealOrder(Integer orderId) {
		try {
			// 参数校验
			if (orderId == null ) {
                return new ResponseBean<>(500, "参数不能为空");
            }


			int orderResult = od.deleteOrder(orderId);
			if (orderResult <= 0 ) {
				return new ResponseBean<>(500, "删除失败");
			}

			return new ResponseBean<>(200, "删除成功", null);
		} catch (Exception e) {
			return new ResponseBean<>(500, "系统错误" );
		}
	}
	
	// 或者返回更友好的格式
    public ResponseBean<Map<String, Boolean>> getDailyMealStatus(Integer customerId, String date) {
        try {
            DailyMealOrderStatus status = od.getDailyMealStatus(customerId, date);
            Map<String, Boolean> result = new LinkedHashMap<>();
            result.put("早餐", status.isHasBreakfast());
            result.put("午餐", status.isHasLunch());
            result.put("晚餐", status.isHasDinner());
            return new ResponseBean<>(200,"查询成功",result);
        } catch (Exception e) {
            return new ResponseBean<>(500, "查询失败" );
        }
    }

}
