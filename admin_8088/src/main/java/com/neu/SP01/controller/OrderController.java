package com.neu.SP01.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.neu.SP01.po.OrderDetail;
import com.neu.SP01.po.ResponseBean;
import com.neu.SP01.service.OrderService;

@CrossOrigin("*")
@RequestMapping("/OrderController")
@RestController
public class OrderController {
	
	@Autowired  // 确保正确注入
    private OrderService os;
	//添加
	 @PostMapping("/addOrder")
	    public ResponseBean<String> createOrder(@RequestBody OrderRequest orderRequest) {
	        return os.createMealOrder(
	        		orderRequest.getCustomerId(),
	        		orderRequest.getMealType(),
	        		orderRequest.getDate(),
	        		orderRequest.getRequest(),
	        		orderRequest.getOrderDetails()
	        );
	    }
	 //删除
	 @PostMapping("/deleteOrder")
	    public ResponseBean<Integer> deleteOrder(@RequestParam  Integer orderId) {
	        return os.deleteMealOrder(orderId);
	    }
	    @GetMapping("/status")
	    public ResponseBean<?> getDailyMealStatus(
	            @RequestParam Integer customerId,
	            @RequestParam String date) {
	        
	         return os.getDailyMealStatus(customerId, date);
	    }

	}

	




	class OrderRequest {
	    private Integer customerId;
	    private Integer mealType; // 1:早餐, 2:午餐, 3:晚餐
	    private String date; // 直接使用Date类型
	    private String request;
	    private List<OrderDetail> orderDetails;
		public Integer getCustomerId() {
			return customerId;
		}
		public void setCustomerId(Integer customerId) {
			this.customerId = customerId;
		}
		public Integer getMealType() {
			return mealType;
		}
		public void setMealType(Integer mealType) {
			this.mealType = mealType;
		}
		public String getDate() {
			return date;
		}
		public void setDate(String date) {
			this.date = date;
		}
		public String getRequest() {
			return request;
		}
		public void setRequest(String request) {
			this.request = request;
		}
		public List<OrderDetail> getOrderDetails() {
			return orderDetails;
		}
		public void setOrderDetails(List<OrderDetail> orderDetails) {
			this.orderDetails = orderDetails;
		}
	    
	}


