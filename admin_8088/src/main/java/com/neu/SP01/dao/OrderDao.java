package com.neu.SP01.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.neu.SP01.po.DailyMealOrderStatus;
import com.neu.SP01.po.Order;
import com.neu.SP01.po.OrderDetail;
@Mapper
public interface OrderDao {
	
	@Insert("INSERT INTO t_order(customer_id, type, state, date, request) " +
            "VALUES(#{customerId}, #{type}, 1, #{date}, #{request, jdbcType=VARCHAR})")
    @Options(useGeneratedKeys = true, keyProperty = "orderId")
    int insertOrder(Order order);
	
	// 插入订单详情
    @Insert("INSERT INTO t_order_detail(order_id, meal_id, count) " +
            "VALUES(#{orderId}, #{mealId}, #{count})")
    int insertOrderDetail(OrderDetail orderDetail);
    
    // 检查是否已存在当日同类型订单
    @Select("SELECT COUNT(*) FROM t_order " +
            "WHERE customer_id = #{customerId} " +
            "AND type = #{type} " +
            "AND date = #{date} " +
            "AND state = 1")
    int countExistingOrder(@Param("customerId") Integer customerId, 
                          @Param("type") Integer type,
                          @Param("date") String date);
    
    //删除
    @Update("UPDATE t_order SET state = 0 WHERE order_id = #{orderId}")
    int deleteOrder(@Param("orderId") Integer orderId);
    
    //查看点餐情况
    @Select("SELECT " +
            "MAX(CASE WHEN type = 0 THEN 1 ELSE 0 END) as has_breakfast, " +
            "MAX(CASE WHEN type = 1 THEN 1 ELSE 0 END) as has_lunch, " +
            "MAX(CASE WHEN type = 2 THEN 1 ELSE 0 END) as has_dinner " +
            "FROM t_order " +
            "WHERE customer_id = #{customerId} AND date = #{date} AND state = 1")
    DailyMealOrderStatus getDailyMealStatus(@Param("customerId") Integer customerId, 
                                          @Param("date") String date);

}
