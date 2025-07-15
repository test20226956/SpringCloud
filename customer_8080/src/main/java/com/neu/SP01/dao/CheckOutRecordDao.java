package com.neu.SP01.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.neu.SP01.po.CheckOutDetailDTO;
import com.neu.SP01.po.CheckOutRecord;
import com.neu.SP01.po.CheckOutRecordWithName;
import com.neu.SP01.po.CustCheckOutDTO;

@Mapper
public interface CheckOutRecordDao {


	/*分页查询退住申请记录*/
	@Select("SELECT r.*, c.name AS customerName ,c.gender,c.identity,u.user_name AS nurseName " +
            "FROM t_check_out_record r " +
            "LEFT JOIN t_customer c ON r.customer_id = c.customer_id " +
            "LEFT JOIN t_user u ON r.nurse_id = u.user_id " +
            "ORDER BY r.apply_time DESC " +
            "LIMIT #{offset}, #{pageSize}")
    List<CheckOutRecordWithName> showCheckOut(@Param("offset") long offset, 
                                            @Param("pageSize") long pageSize);
	/*查询退住申请总记录数*/
	@Select("SELECT COUNT(*) FROM t_check_out_record r " +
	        "LEFT JOIN t_customer c ON r.customer_id = c.customer_id " +
	        "LEFT JOIN t_user u ON r.nurse_id = u.user_id")
	long countCheckOut();
	
	/*分页多条件查询*/
	@Select("<script>" +
            "SELECT r.*, c.name AS customerName ,c.gender,c.identity,u.user_name AS nurseName " +
            "FROM t_check_out_record r " +
            "LEFT JOIN t_customer c ON r.customer_id = c.customer_id " +
            "LEFT JOIN t_user u ON r.nurse_id = u.user_id " +
            "<where>" +
            "   <if test='customerName != null and customerName != \"\"'>" +
            "       AND c.name LIKE CONCAT('%', #{customerName}, '%')" +
            "   </if>" +
            "   <if test='state != null'>" +
            "       AND r.state = #{state}" +
            "   </if>" +
            "   <if test='applyTime != null and applyTime != \"\"'>" +
            "       AND r.apply_time >= #{applyTime}" +
            "   </if>" +
            "</where>" +
            "ORDER BY r.apply_time DESC " +
            "LIMIT #{offset}, #{pageSize}" +
            "</script>")
    List<CheckOutRecordWithName> queryByConditions(
            @Param("customerName") String customerName,
            @Param("state") Integer state,
            @Param("applyTime") String applyTime,
            @Param("offset") long offset,
            @Param("pageSize") long pageSize);

    /*多条件查询总数*/
    @Select("<script>" +
            "SELECT COUNT(*) " +
            "FROM t_check_out_record r " +
            "LEFT JOIN t_customer c ON r.customer_id = c.customer_id " +
            "LEFT JOIN t_user u ON r.nurse_id = u.user_id " +
            "<where>" +
            "   <if test='customerName != null and customerName != \"\"'>" +
            "       AND c.name LIKE CONCAT('%', #{customerName}, '%')" +
            "   </if>" +
            "   <if test='state != null'>" +
            "       AND r.state = #{state}" +
            "   </if>" +
            "   <if test='applyTime != null and applyTime != \"\"'>" +
            "       AND r.apply_time >= #{applyTime}" +
            "   </if>" +
            "</where>" +
            "</script>")
    long countByConditions(
            @Param("customerName") String customerName,
            @Param("state") Integer state,
            @Param("applyTime") String applyTime);
    
    /*审核*/
    @Update("UPDATE t_check_out_record " +
            "SET state = #{state}, " +
            "admin_id = #{adminId}, " +
            "examine_time = #{examineTime} " +
            "WHERE check_out_record_id = #{checkOutRecordId}")
    int approveCheckOut(@Param("checkOutRecordId") Integer checkOutRecordId,
                       @Param("state") Integer state,
                       @Param("adminId") Integer adminId,
                       @Param("examineTime") String examineTime);
    
    /*根据退住记录ID获取客户ID*/
    @Select("SELECT customer_id FROM t_check_out_record WHERE check_out_record_id = #{checkOutRecordId}")
    Integer findCustomerIdByCheckOutId(@Param("checkOutRecordId") Integer checkOutRecordId);
	
    //查询详情 根据ID
    @Select("SELECT cor.*, " +  // 包含t_check_out_record所有字段
            "c.name AS customerName, " +
            "c.gender, " +
            "c.identity, " +
            "r.building_number AS building, " +
            "r.floor, " +
            "r.room_number AS roomNumber, " +
            "b.bed_number AS bedNumber, " +
            "u.user_name AS nurseName " +
            "FROM t_check_out_record cor " +
            "JOIN t_customer c ON cor.customer_id = c.customer_id " +
            "JOIN t_check_in_record cir ON c.customer_id = cir.customer_id " +
            "JOIN t_bed_record br ON cir.check_in_record_id = br.check_in_record_id " +
            "JOIN t_bed b ON br.bed_id = b.bed_id " +
            "JOIN t_room r ON b.room_id = r.room_id " +
            "JOIN t_user u ON u.user_id = cor.nurse_id " +
            "WHERE cor.check_out_record_id = #{checkOutRecordId} " +
            "AND cir.state = 1 "+
            "AND br.state=1 ")
    CheckOutDetailDTO getCheckOutDetailById(@Param("checkOutRecordId") Integer checkOutRecordId);
    //查今天退住的人
    @Select("SELECT COUNT(*) FROM t_check_out_record " +
            "WHERE state=1 AND examine_time=#{today} ")
    int countTodayCheckOuts(@Param("today") String today);
	
    
    
	
	
    

	/*====护工模块 用户管理退住申请====*/
	//添加用户的退住申请
	@Insert("insert into yyzx_st.t_check_out_record values (null,#{customerId},0,#{type},#{applyTime},#{examineTime},#{adminId},#{nurseId},#{reason})")
	void InsertCheckOutRecord(CheckOutRecord cor);
	//查看用户的退住申请记录
	List<CustCheckOutDTO> findCheckOutRecordByCustomerId(Integer customerId);

}
