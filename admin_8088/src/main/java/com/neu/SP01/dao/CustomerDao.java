package com.neu.SP01.dao;

import java.util.List;


import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.neu.SP01.po.CustCheckInDTO;
import com.neu.SP01.po.CustCheckInNurseDTO;
import com.neu.SP01.po.CustDailyNursingDTO;
import com.neu.SP01.po.CustNursingManageDTO;
import com.neu.SP01.po.Customer;


@Mapper
public interface CustomerDao {
	/*查询所有自理老人客户列表*/
	
	@Select("SELECT " +
	        "cir.check_in_record_id, cir.customer_id, cir.nursing_level_id, cir.user_id, " +
	        "cir.check_in_time, cir.end_time, cir.state, " +
	        
	        "c.customer_id as c_customer_id, c.name as c_name, c.type as c_type, " +
	        "c.gender as c_gender, c.identity as c_identity, " +
	        "c.blood_type as c_blood_type, c.tel as c_tel, " +
	        
	        "f.family_id as f_family_id, f.customer_id as f_customer_id, " +
	        "f.name as f_name, f.relation as f_relation, f.tel as f_tel,f.state as f_state, " +
	        
	        "br.bed_record_id as br_bed_record_id, br.bed_id as br_bed_id, " +
	        "br.check_in_record_id as br_check_in_record_id, br.state as br_state, " +
	        "br.start_time as br_start_time, br.end_time as br_end_time, " +
	        
	        "b.bed_id as b_bed_id, b.room_id as b_room_id, b.bed_number as b_bed_number, b.available as b_available, " +
	        "r.room_id as r_room_id, r.room_number as r_room_number, r.building_number as r_building_number, " +
	        "r.floor as r_floor, r.bed_count as r_bed_count, " +
	        "TIMESTAMPDIFF(YEAR, STR_TO_DATE(SUBSTRING(c.identity, 7, 8), '%Y%m%d'), CURDATE()) as age " +
	        "FROM t_customer c " +
	        "JOIN t_check_in_record cir ON c.customer_id = cir.customer_id " +
	        "LEFT JOIN t_family f ON c.customer_id = f.customer_id AND f.state=1 " +
	        "JOIN t_bed_record br ON cir.check_in_record_id = br.check_in_record_id AND br.state = 1 " +
	        "JOIN t_bed b ON br.bed_id = b.bed_id " +
	        "JOIN t_room r ON b.room_id = r.room_id " +
	        "WHERE c.type = '0' AND cir.state = 1 " +
	        "LIMIT #{offset}, #{pageSize}")
	@Results({
	    // 主对象映射
	    @Result(property = "age", column = "age"),
	    
	    // CheckInRecord映射
	    @Result(property = "checkInRecord.checkInRecordId", column = "check_in_record_id"),
	    @Result(property = "checkInRecord.customerId", column = "customer_id"),
	    @Result(property = "checkInRecord.nursingLevelId", column = "nursing_level_id"),
	    @Result(property = "checkInRecord.userId", column = "user_id"),
	    @Result(property = "checkInRecord.checkInTime", column = "check_in_time"),
	    @Result(property = "checkInRecord.endTime", column = "end_time"),
	    @Result(property = "checkInRecord.state", column = "state"),
	    
	    // Customer映射
	    @Result(property = "customer.customerId", column = "c_customer_id"),
	    @Result(property = "customer.name", column = "c_name"),
	    @Result(property = "customer.type", column = "c_type"),
	    @Result(property = "customer.gender", column = "c_gender"),
	    @Result(property = "customer.identity", column = "c_identity"),
	    @Result(property = "customer.bloodType", column = "c_blood_type"),
	    @Result(property = "customer.tel", column = "c_tel"),
	    
	    // Family映射
	    @Result(property = "family.familyId", column = "f_family_id"),
	    @Result(property = "family.customerId", column = "f_customer_id"),
	    @Result(property = "family.name", column = "f_name"),
	    @Result(property = "family.relation", column = "f_relation"),
	    @Result(property = "family.tel", column = "f_tel"),
	    @Result(property = "family.state", column = "f_state"),
	    
	    // BedRecord映射
	    @Result(property = "bedRecord.bedRecordId", column = "br_bed_record_id"),
	    @Result(property = "bedRecord.bedId", column = "br_bed_id"),
	    @Result(property = "bedRecord.checkInRecordId", column = "br_check_in_record_id"),
	    @Result(property = "bedRecord.state", column = "br_state"),
	    @Result(property = "bedRecord.startTime", column = "br_start_time"),
	    @Result(property = "bedRecord.endTime", column = "br_end_time"),
	    
	    // Bed映射
	    @Result(property = "bed.bedId", column = "b_bed_id"),
	    @Result(property = "bed.roomId", column = "b_room_id"),
	    @Result(property = "bed.bedNumber", column = "b_bed_number"),
	    @Result(property = "bed.available", column = "b_available"),
	    
	    // Room映射
	    @Result(property = "room.roomId", column = "r_room_id"),
	    @Result(property = "room.roomNumber", column = "r_room_number"),
	    @Result(property = "room.buildingNumber", column = "r_building_number"),
	    @Result(property = "room.floor", column = "r_floor"),
	    @Result(property = "room.bedCount", column = "r_bed_count")
	})
    List<CustCheckInDTO> showSelfCust(@Param("offset")long offset, @Param("pageSize")long pageSize);
	/*计算总数*/
	@Select("SELECT COUNT(*) FROM t_customer c " +
	        "JOIN t_check_in_record cir ON c.customer_id = cir.customer_id " +
	        "LEFT JOIN t_family f ON c.customer_id = f.customer_id AND f.state = 1 " +
	        "JOIN t_bed_record br ON cir.check_in_record_id = br.check_in_record_id AND br.state = 1 " +
	        "JOIN t_bed b ON br.bed_id = b.bed_id " +
	        "JOIN t_room r ON b.room_id = r.room_id " +
	        "WHERE c.type = '0' AND cir.state = 1")
	long countSelfCareCustomers();
	
	
	/*查询所有护理老人客户列表*/
	@Select("SELECT " +
	        "cir.check_in_record_id, cir.customer_id, cir.nursing_level_id, cir.user_id, " +
	        "cir.check_in_time, cir.end_time, cir.state, " +
	        
	        "c.customer_id as c_customer_id, c.name as c_name, c.type as c_type, " +
	        "c.gender as c_gender, c.identity as c_identity, " +
	        "c.blood_type as c_blood_type, c.tel as c_tel, " +
	        
	        "f.family_id as f_family_id, f.customer_id as f_customer_id, " +
	        "f.name as f_name, f.relation as f_relation, f.tel as f_tel,f.state as f_state, " +
	        
	        "br.bed_record_id as br_bed_record_id, br.bed_id as br_bed_id, " +
	        "br.check_in_record_id as br_check_in_record_id, br.state as br_state, " +
	        "br.start_time as br_start_time, br.end_time as br_end_time, " +
	        
	        "b.bed_id as b_bed_id, b.room_id as b_room_id, b.bed_number as b_bed_number, b.available as b_available, " +
	        "r.room_id as r_room_id, r.room_number as r_room_number, r.building_number as r_building_number, " +
	        "r.floor as r_floor, r.bed_count as r_bed_count, " +
	        "nl.nursing_level_id as nl_nursing_level_id, nl.name as nl_name, nl.state as nl_state, " +
	        "TIMESTAMPDIFF(YEAR, STR_TO_DATE(SUBSTRING(c.identity, 7, 8), '%Y%m%d'), CURDATE()) as age " +
	        "FROM t_customer c " +
	        "JOIN t_check_in_record cir ON c.customer_id = cir.customer_id " +
	        "LEFT JOIN t_family f ON c.customer_id = f.customer_id AND f.state=1 " +
	        "LEFT JOIN t_bed_record br ON cir.check_in_record_id = br.check_in_record_id AND br.state = 1 " +
	        "LEFT JOIN t_bed b ON br.bed_id = b.bed_id " +
	        "LEFT JOIN t_room r ON b.room_id = r.room_id " +
	        "LEFT JOIN t_nursing_level nl ON nl.nursing_level_id = cir.nursing_level_id " +
	        "WHERE c.type = '1' AND cir.state = 1 " +
	        "LIMIT #{offset}, #{pageSize}")
	@Results({
	    // 主对象映射
	    @Result(property = "age", column = "age"),
	    
	    // CheckInRecord映射
	    @Result(property = "checkInRecord.checkInRecordId", column = "check_in_record_id"),
	    @Result(property = "checkInRecord.customerId", column = "customer_id"),
	    @Result(property = "checkInRecord.nursingLevelId", column = "nursing_level_id"),
	    @Result(property = "checkInRecord.userId", column = "user_id"),
	    @Result(property = "checkInRecord.checkInTime", column = "check_in_time"),
	    @Result(property = "checkInRecord.endTime", column = "end_time"),
	    @Result(property = "checkInRecord.state", column = "state"),
	    
	    // Customer映射
	    @Result(property = "customer.customerId", column = "c_customer_id"),
	    @Result(property = "customer.name", column = "c_name"),
	    @Result(property = "customer.type", column = "c_type"),
	    @Result(property = "customer.gender", column = "c_gender"),
	    @Result(property = "customer.identity", column = "c_identity"),
	    @Result(property = "customer.bloodType", column = "c_blood_type"),
	    @Result(property = "customer.tel", column = "c_tel"),
	    
	    // Family映射
	    @Result(property = "family.familyId", column = "f_family_id"),
	    @Result(property = "family.customerId", column = "f_customer_id"),
	    @Result(property = "family.name", column = "f_name"),
	    @Result(property = "family.relation", column = "f_relation"),
	    @Result(property = "family.tel", column = "f_tel"),
	    @Result(property = "family.state", column = "f_state"),
	    
	    // BedRecord映射
	    @Result(property = "bedRecord.bedRecordId", column = "br_bed_record_id"),
	    @Result(property = "bedRecord.bedId", column = "br_bed_id"),
	    @Result(property = "bedRecord.checkInRecordId", column = "br_check_in_record_id"),
	    @Result(property = "bedRecord.state", column = "br_state"),
	    @Result(property = "bedRecord.startTime", column = "br_start_time"),
	    @Result(property = "bedRecord.endTime", column = "br_end_time"),
	    
	    // Bed映射
	    @Result(property = "bed.bedId", column = "b_bed_id"),
	    @Result(property = "bed.roomId", column = "b_room_id"),
	    @Result(property = "bed.bedNumber", column = "b_bed_number"),
	    @Result(property = "bed.available", column = "b_available"),
	    
	    // Room映射
	    @Result(property = "room.roomId", column = "r_room_id"),
	    @Result(property = "room.roomNumber", column = "r_room_number"),
	    @Result(property = "room.buildingNumber", column = "r_building_number"),
	    @Result(property = "room.floor", column = "r_floor"),
	    @Result(property = "room.bedCount", column = "r_bed_count"),
	    
	 // NursingLevel映射
	    @Result(property = "nursingLevel.nursingLevelId", column = "nl_nursing_level_id"),
	    @Result(property = "nursingLevel.name", column = "nl_name"),
	    @Result(property = "nursingLevel.state", column = "nl_state")
	})
	List<CustCheckInNurseDTO> showCareCust(long offset,long pageSize);
	@Select("SELECT COUNT(*) FROM t_customer c " +
	        "JOIN t_check_in_record cir ON c.customer_id = cir.customer_id " +
	        "LEFT JOIN t_family f ON c.customer_id = f.customer_id AND f.state = 1 " +
	        "JOIN t_bed_record br ON cir.check_in_record_id = br.check_in_record_id AND br.state = 1 " +
	        "JOIN t_bed b ON br.bed_id = b.bed_id " +
	        "JOIN t_room r ON b.room_id = r.room_id " +
	        "WHERE c.type = '1' AND cir.state = 1")
    long countCareCustomers();
	
	
	/*多条件组合查询客户信息*/
	@Select("<script>" +
			"SELECT " +
	        "cir.check_in_record_id, cir.customer_id, cir.nursing_level_id, cir.user_id, " +
	        "cir.check_in_time, cir.end_time, cir.state, " +
	        
	        "c.customer_id as c_customer_id, c.name as c_name, c.type as c_type, " +
	        "c.gender as c_gender, c.identity as c_identity, " +
	        "c.blood_type as c_blood_type, c.tel as c_tel, " +
	        
	        "f.family_id as f_family_id, f.customer_id as f_customer_id, " +
	        "f.name as f_name, f.relation as f_relation, f.tel as f_tel,f.state as f_state, " +
	        
	        "br.bed_record_id as br_bed_record_id, br.bed_id as br_bed_id, " +
	        "br.check_in_record_id as br_check_in_record_id, br.state as br_state, " +
	        "br.start_time as br_start_time, br.end_time as br_end_time, " +
	        
	        "b.bed_id as b_bed_id, b.room_id as b_room_id, b.bed_number as b_bed_number, b.available as b_available, " +
	        "r.room_id as r_room_id, r.room_number as r_room_number, r.building_number as r_building_number, " +
	        "r.floor as r_floor, r.bed_count as r_bed_count, " +
	        "TIMESTAMPDIFF(YEAR, STR_TO_DATE(SUBSTRING(c.identity, 7, 8), '%Y%m%d'), CURDATE()) as age " +
	        "FROM t_customer c " +
	        "JOIN t_check_in_record cir ON c.customer_id = cir.customer_id " +
	        "LEFT JOIN t_family f ON c.customer_id = f.customer_id AND f.state=1 " +
	        "LEFT JOIN t_bed_record br ON cir.check_in_record_id = br.check_in_record_id AND br.state = 1 " +
	        "LEFT JOIN t_bed b ON br.bed_id = b.bed_id " +
	        "LEFT JOIN t_room r ON b.room_id = r.room_id " +
	        "WHERE cir.state = 1 " +
	        "<if test='selfCare != null'>" +
	        "   AND c.type = #{selfCare} " +
	        "</if>" +
	        "<if test='name != null and name != \"\"'>" +
	        "   AND c.name LIKE CONCAT('%', #{name}, '%') " +
	        "</if>" +
	        "<if test='checkInTime != null and checkInTime != \"\"'>" +
	        "   AND cir.check_in_time >= #{checkInTime} " +
	        "</if>" +
	        "LIMIT #{offset}, #{pageSize}" +
	        "</script>")
	@Results({
	    // 主对象映射
	    @Result(property = "age", column = "age"),
	    
	    // CheckInRecord映射
	    @Result(property = "checkInRecord.checkInRecordId", column = "check_in_record_id"),
	    @Result(property = "checkInRecord.customerId", column = "customer_id"),
	    @Result(property = "checkInRecord.nursingLevelId", column = "nursing_level_id"),
	    @Result(property = "checkInRecord.userId", column = "user_id"),
	    @Result(property = "checkInRecord.checkInTime", column = "check_in_time"),
	    @Result(property = "checkInRecord.endTime", column = "end_time"),
	    @Result(property = "checkInRecord.state", column = "state"),
	    
	    // Customer映射
	    @Result(property = "customer.customerId", column = "c_customer_id"),
	    @Result(property = "customer.name", column = "c_name"),
	    @Result(property = "customer.type", column = "c_type"),
	    @Result(property = "customer.gender", column = "c_gender"),
	    @Result(property = "customer.identity", column = "c_identity"),
	    @Result(property = "customer.bloodType", column = "c_blood_type"),
	    @Result(property = "customer.tel", column = "c_tel"),
	    
	    // Family映射
	    @Result(property = "family.familyId", column = "f_family_id"),
	    @Result(property = "family.customerId", column = "f_customer_id"),
	    @Result(property = "family.name", column = "f_name"),
	    @Result(property = "family.relation", column = "f_relation"),
	    @Result(property = "family.tel", column = "f_tel"),
	    @Result(property = "family.state", column = "f_state"),
	    
	    // BedRecord映射
	    @Result(property = "bedRecord.bedRecordId", column = "br_bed_record_id"),
	    @Result(property = "bedRecord.bedId", column = "br_bed_id"),
	    @Result(property = "bedRecord.checkInRecordId", column = "br_check_in_record_id"),
	    @Result(property = "bedRecord.state", column = "br_state"),
	    @Result(property = "bedRecord.startTime", column = "br_start_time"),
	    @Result(property = "bedRecord.endTime", column = "br_end_time"),
	    
	    // Bed映射
	    @Result(property = "bed.bedId", column = "b_bed_id"),
	    @Result(property = "bed.roomId", column = "b_room_id"),
	    @Result(property = "bed.bedNumber", column = "b_bed_number"),
	    @Result(property = "bed.available", column = "b_available"),
	    
	    // Room映射
	    @Result(property = "room.roomId", column = "r_room_id"),
	    @Result(property = "room.roomNumber", column = "r_room_number"),
	    @Result(property = "room.buildingNumber", column = "r_building_number"),
	    @Result(property = "room.floor", column = "r_floor"),
	    @Result(property = "room.bedCount", column = "r_bed_count")
	})
	List<CustCheckInDTO> searchCustomers(@Param("selfCare") String selfCare, 
	                                   @Param("name") String name, 
	                                   @Param("checkInTime") String checkInTime,
	                                   @Param("offset") long offset, 
	                                   @Param("pageSize") long pageSize);

	@Select("<script>" +
	        "SELECT COUNT(*) FROM t_customer c " +
	        "JOIN t_check_in_record cir ON c.customer_id = cir.customer_id " +
	        "LEFT JOIN t_family f ON c.customer_id = f.customer_id AND f.state = 1 " +
	        "LEFT JOIN t_bed_record br ON cir.check_in_record_id = br.check_in_record_id AND br.state = 1 " +
	        "LEFT JOIN t_bed b ON br.bed_id = b.bed_id " +
	        "LEFT JOIN t_room r ON b.room_id = r.room_id " +
	        "WHERE cir.state = 1 " +
	        "<if test='selfCare != null'>" +
	        "   AND c.type = #{selfCare} " +
	        "</if>" +
	        "<if test='name != null and name != \"\"'>" +
	        "   AND c.name LIKE CONCAT('%', #{name}, '%') " +
	        "</if>" +
	        "<if test='checkInTime != null and checkInTime != \"\"'>" +
	        "   AND cir.check_in_time >= #{checkInTime} " +
	        "</if>" +
	        "</script>")
	long countSearchCustomers(@Param("selfCare") String selfCare, 
	                         @Param("name") String name, 
	                         @Param("checkInTime") String checkInTime);
	
	/*多条件组合查询护理客户信息*/
	@Select("<script>" +
	        "SELECT " +
	        "cir.check_in_record_id, cir.customer_id, cir.nursing_level_id, cir.user_id, " +
	        "cir.check_in_time, cir.end_time, cir.state, " +
	        
	        "c.customer_id as c_customer_id, c.name as c_name, c.type as c_type, " +
	        "c.gender as c_gender, c.identity as c_identity, " +
	        "c.blood_type as c_blood_type, c.tel as c_tel, " +
	        
	        "f.family_id as f_family_id, f.customer_id as f_customer_id, " +
	        "f.name as f_name, f.relation as f_relation, f.tel as f_tel,f.state as f_state, " +
	        
	        "br.bed_record_id as br_bed_record_id, br.bed_id as br_bed_id, " +
	        "br.check_in_record_id as br_check_in_record_id, br.state as br_state, " +
	        "br.start_time as br_start_time, br.end_time as br_end_time, " +
	        
	        "b.bed_id as b_bed_id, b.room_id as b_room_id, b.bed_number as b_bed_number, b.available as b_available, " +
	        "r.room_id as r_room_id, r.room_number as r_room_number, r.building_number as r_building_number, " +
	        "r.floor as r_floor, r.bed_count as r_bed_count, " +
	        "nl.nursing_level_id as nl_nursing_level_id, nl.name as nl_name, nl.state as nl_state, " +
	        "TIMESTAMPDIFF(YEAR, STR_TO_DATE(SUBSTRING(c.identity, 7, 8), '%Y%m%d'), CURDATE()) as age " +
	        "FROM t_customer c " +
	        "JOIN t_check_in_record cir ON c.customer_id = cir.customer_id " +
	        "LEFT JOIN t_family f ON c.customer_id = f.customer_id AND f.state=1 " +
	        "LEFT JOIN t_bed_record br ON cir.check_in_record_id = br.check_in_record_id AND br.state = 1 " +
	        "LEFT JOIN t_bed b ON br.bed_id = b.bed_id " +
	        "LEFT JOIN t_room r ON b.room_id = r.room_id " +
	        "LEFT JOIN t_nursing_level nl ON nl.nursing_level_id = cir.nursing_level_id " +
	        "WHERE cir.state = 1 AND c.type=1" +
	        "<if test='name != null and name != \"\"'>" +
	        "   AND c.name LIKE CONCAT('%', #{name}, '%') " +
	        "</if>" +
	        "<if test='checkInTime != null and checkInTime != \"\"'>" +
	        "   AND cir.check_in_time >= #{checkInTime} " +
	        "</if>" +
	        "<if test='nursingLevelId != null and nursingLevelId != \"\"'>" +
	        "   AND cir.nursing_level_id= #{nursingLevelId} " +
	        "</if>" +
	        "LIMIT #{offset}, #{pageSize}" +
	        "</script>")
	@Results({
	    // 主对象映射
	    @Result(property = "age", column = "age"),
	    
	    // CheckInRecord映射
	    @Result(property = "checkInRecord.checkInRecordId", column = "check_in_record_id"),
	    @Result(property = "checkInRecord.customerId", column = "customer_id"),
	    @Result(property = "checkInRecord.nursingLevelId", column = "nursing_level_id"),
	    @Result(property = "checkInRecord.userId", column = "user_id"),
	    @Result(property = "checkInRecord.checkInTime", column = "check_in_time"),
	    @Result(property = "checkInRecord.endTime", column = "end_time"),
	    @Result(property = "checkInRecord.state", column = "state"),
	    
	    // Customer映射
	    @Result(property = "customer.customerId", column = "c_customer_id"),
	    @Result(property = "customer.name", column = "c_name"),
	    @Result(property = "customer.type", column = "c_type"),
	    @Result(property = "customer.gender", column = "c_gender"),
	    @Result(property = "customer.identity", column = "c_identity"),
	    @Result(property = "customer.bloodType", column = "c_blood_type"),
	    @Result(property = "customer.tel", column = "c_tel"),
	    
	    // Family映射
	    @Result(property = "family.familyId", column = "f_family_id"),
	    @Result(property = "family.customerId", column = "f_customer_id"),
	    @Result(property = "family.name", column = "f_name"),
	    @Result(property = "family.relation", column = "f_relation"),
	    @Result(property = "family.tel", column = "f_tel"),
	    @Result(property = "family.state", column = "f_state"),
	    
	    // BedRecord映射
	    @Result(property = "bedRecord.bedRecordId", column = "br_bed_record_id"),
	    @Result(property = "bedRecord.bedId", column = "br_bed_id"),
	    @Result(property = "bedRecord.checkInRecordId", column = "br_check_in_record_id"),
	    @Result(property = "bedRecord.state", column = "br_state"),
	    @Result(property = "bedRecord.startTime", column = "br_start_time"),
	    @Result(property = "bedRecord.endTime", column = "br_end_time"),
	    
	    // Bed映射
	    @Result(property = "bed.bedId", column = "b_bed_id"),
	    @Result(property = "bed.roomId", column = "b_room_id"),
	    @Result(property = "bed.bedNumber", column = "b_bed_number"),
	    @Result(property = "bed.available", column = "b_available"),
	    
	    // Room映射
	    @Result(property = "room.roomId", column = "r_room_id"),
	    @Result(property = "room.roomNumber", column = "r_room_number"),
	    @Result(property = "room.buildingNumber", column = "r_building_number"),
	    @Result(property = "room.floor", column = "r_floor"),
	    @Result(property = "room.bedCount", column = "r_bed_count"),
	    
	    // NursingLevel映射 - 条件性映射
	    @Result(property = "nursingLevel.nursingLevelId", column = "nl_nursing_level_id"),
	    @Result(property = "nursingLevel.name", column = "nl_name"),
	    @Result(property = "nursingLevel.state", column = "nl_state")
	})
	List<CustCheckInNurseDTO> searchCareCustomers(@Param("name") String name, 
	                                   @Param("checkInTime") String checkInTime,
	                                   @Param("nursingLevelId") Integer nursingLevelId,
	                                   @Param("offset") long offset, 
	                                   @Param("pageSize") long pageSize);

	@Select("<script>" +
	        "SELECT COUNT(*) FROM t_customer c " +
	        "JOIN t_check_in_record cir ON c.customer_id = cir.customer_id " +
	        "LEFT JOIN t_family f ON c.customer_id = f.customer_id AND f.state = 1 " +
	        "LEFT JOIN t_bed_record br ON cir.check_in_record_id = br.check_in_record_id AND br.state = 1 " +
	        "LEFT JOIN t_bed b ON br.bed_id = b.bed_id " +
	        "LEFT JOIN t_room r ON b.room_id = r.room_id " +
	        "LEFT JOIN t_nursing_level nl ON nl.nursing_level_id = cir.nursing_level_id " +
	        "WHERE cir.state = 1 AND c.type = 1 " +
	        "<if test='name != null and name != \"\"'>" +
	        "   AND c.name LIKE CONCAT('%', #{name}, '%') " +
	        "</if>" +
	        "<if test='checkInTime != null and checkInTime != \"\"'>" +
	        "   AND cir.check_in_time >= #{checkInTime} " +
	        "</if>" +
	        "<if test='nursingLevelId != null'>" +
	        "   AND cir.nursing_level_id = #{nursingLevelId} " +
	        "</if>" +
	        "</script>")
	long countSearchCareCustomers(@Param("name") String name, 
	                           @Param("checkInTime") String checkInTime,
	                           @Param("nursingLevelId") Integer nursingLevelId);
	

	/*多条件组合查询没有护理级别的护理客户信息*/
	@Select("<script>" +
	        "SELECT " +
	        "cir.check_in_record_id, cir.customer_id, cir.nursing_level_id, cir.user_id, " +
	        "cir.check_in_time, cir.end_time, cir.state, " +
	        
	        "c.customer_id as c_customer_id, c.name as c_name, c.type as c_type, " +
	        "c.gender as c_gender, c.identity as c_identity, " +
	        "c.blood_type as c_blood_type, c.tel as c_tel, " +
	        
	        "f.family_id as f_family_id, f.customer_id as f_customer_id, " +
	        "f.name as f_name, f.relation as f_relation, f.tel as f_tel,f.state as f_state, " +
	        
	        "br.bed_record_id as br_bed_record_id, br.bed_id as br_bed_id, " +
	        "br.check_in_record_id as br_check_in_record_id, br.state as br_state, " +
	        "br.start_time as br_start_time, br.end_time as br_end_time, " +
	        
	        "b.bed_id as b_bed_id, b.room_id as b_room_id, b.bed_number as b_bed_number, b.available as b_available, " +
	        "r.room_id as r_room_id, r.room_number as r_room_number, r.building_number as r_building_number, " +
	        "r.floor as r_floor, r.bed_count as r_bed_count, " +
	        "nl.nursing_level_id as nl_nursing_level_id, nl.name as nl_name, nl.state as nl_state, " +
	        "TIMESTAMPDIFF(YEAR, STR_TO_DATE(SUBSTRING(c.identity, 7, 8), '%Y%m%d'), CURDATE()) as age " +
	        "FROM t_customer c " +
	        "JOIN t_check_in_record cir ON c.customer_id = cir.customer_id " +
	        "LEFT JOIN t_family f ON c.customer_id = f.customer_id AND f.state=1 " +
	        "LEFT JOIN t_bed_record br ON cir.check_in_record_id = br.check_in_record_id AND br.state = 1 " +
	        "LEFT JOIN t_bed b ON br.bed_id = b.bed_id " +
	        "LEFT JOIN t_room r ON b.room_id = r.room_id " +
	        "LEFT JOIN t_nursing_level nl ON nl.nursing_level_id = cir.nursing_level_id " +
	        "WHERE cir.state = 1 AND c.type=1 AND cir.nursing_level_id IS NULL" +
	        "<if test='name != null and name != \"\"'>" +
	        "   AND c.name LIKE CONCAT('%', #{name}, '%') " +
	        "</if>" +
	        "<if test='checkInTime != null and checkInTime != \"\"'>" +
	        "   AND cir.check_in_time >= #{checkInTime} " +
	        "</if>" +
	        "LIMIT #{offset}, #{pageSize}" +
	        "</script>")
	@Results({
	    // 主对象映射
	    @Result(property = "age", column = "age"),
	    
	    // CheckInRecord映射
	    @Result(property = "checkInRecord.checkInRecordId", column = "check_in_record_id"),
	    @Result(property = "checkInRecord.customerId", column = "customer_id"),
	    @Result(property = "checkInRecord.nursingLevelId", column = "nursing_level_id"),
	    @Result(property = "checkInRecord.userId", column = "user_id"),
	    @Result(property = "checkInRecord.checkInTime", column = "check_in_time"),
	    @Result(property = "checkInRecord.endTime", column = "end_time"),
	    @Result(property = "checkInRecord.state", column = "state"),
	    
	    // Customer映射
	    @Result(property = "customer.customerId", column = "c_customer_id"),
	    @Result(property = "customer.name", column = "c_name"),
	    @Result(property = "customer.type", column = "c_type"),
	    @Result(property = "customer.gender", column = "c_gender"),
	    @Result(property = "customer.identity", column = "c_identity"),
	    @Result(property = "customer.bloodType", column = "c_blood_type"),
	    @Result(property = "customer.tel", column = "c_tel"),
	    
	    // Family映射
	    @Result(property = "family.familyId", column = "f_family_id"),
	    @Result(property = "family.customerId", column = "f_customer_id"),
	    @Result(property = "family.name", column = "f_name"),
	    @Result(property = "family.relation", column = "f_relation"),
	    @Result(property = "family.tel", column = "f_tel"),
	    @Result(property = "family.state", column = "f_state"),
	    
	    // BedRecord映射
	    @Result(property = "bedRecord.bedRecordId", column = "br_bed_record_id"),
	    @Result(property = "bedRecord.bedId", column = "br_bed_id"),
	    @Result(property = "bedRecord.checkInRecordId", column = "br_check_in_record_id"),
	    @Result(property = "bedRecord.state", column = "br_state"),
	    @Result(property = "bedRecord.startTime", column = "br_start_time"),
	    @Result(property = "bedRecord.endTime", column = "br_end_time"),
	    
	    // Bed映射
	    @Result(property = "bed.bedId", column = "b_bed_id"),
	    @Result(property = "bed.roomId", column = "b_room_id"),
	    @Result(property = "bed.bedNumber", column = "b_bed_number"),
	    @Result(property = "bed.available", column = "b_available"),
	    
	    // Room映射
	    @Result(property = "room.roomId", column = "r_room_id"),
	    @Result(property = "room.roomNumber", column = "r_room_number"),
	    @Result(property = "room.buildingNumber", column = "r_building_number"),
	    @Result(property = "room.floor", column = "r_floor"),
	    @Result(property = "room.bedCount", column = "r_bed_count"),
	    
	    // NursingLevel映射 - 条件性映射
	    @Result(property = "nursingLevel.nursingLevelId", column = "nl_nursing_level_id"),
	    @Result(property = "nursingLevel.name", column = "nl_name"),
	    @Result(property = "nursingLevel.state", column = "nl_state")
	})
	List<CustCheckInNurseDTO> searchNoLevelCareCustomers(@Param("name") String name, 
	                                   @Param("checkInTime") String checkInTime,
	                                   @Param("offset") long offset, 
	                                   @Param("pageSize") long pageSize);

	@Select("<script>" +
	        "SELECT COUNT(*) FROM t_customer c " +
	        "JOIN t_check_in_record cir ON c.customer_id = cir.customer_id " +
	        "LEFT JOIN t_family f ON c.customer_id = f.customer_id AND f.state = 1 " +
	        "LEFT JOIN t_bed_record br ON cir.check_in_record_id = br.check_in_record_id AND br.state = 1 " +
	        "LEFT JOIN t_bed b ON br.bed_id = b.bed_id " +
	        "LEFT JOIN t_room r ON b.room_id = r.room_id " +
	        "LEFT JOIN t_nursing_level nl ON nl.nursing_level_id = cir.nursing_level_id " +
	        "WHERE cir.state = 1 AND c.type = 1 AND cir.nursing_level_id IS NULL " +
	        "<if test='name != null and name != \"\"'>" +
	        "   AND c.name LIKE CONCAT('%', #{name}, '%') " +
	        "</if>" +
	        "<if test='checkInTime != null and checkInTime != \"\"'>" +
	        "   AND cir.check_in_time >= #{checkInTime} " +
	        "</if>" +
	        "</script>")
	long countSearchNoLevelCareCustomers(@Param("name") String name, 
	                                   @Param("checkInTime") String checkInTime);
	
	
	/*根据身份证号查老人信息*/
	@Select("SELECT * FROM t_customer WHERE identity = #{identity}")
	Customer searchCustByIdentity(String identity);
	
	
	// 添加老人信息（包含密码）
    @Insert("INSERT INTO t_customer(name, type, gender, identity, blood_type, tel, password) " +
            "VALUES(#{customer.name}, #{customer.type}, #{customer.gender}, " +
            "#{customer.identity}, #{customer.bloodType}, #{customer.tel}, #{customer.password})")
    @Options(useGeneratedKeys = true, keyProperty = "customer.customerId")
    int insertCustomer(@Param("customer") Customer customer);
	
	
	
 // 更新客户信息
    @Update("UPDATE t_customer SET name=#{name}, type=#{type}, gender=#{gender}, " +
            "identity=#{identity}, blood_type=#{bloodType}, tel=#{tel} " +
            "WHERE customer_id=#{customerId}")
    int updateCustomer(Customer customer);
    
    
    
  //展示所有没有管家的护理老人
    
  	@Select("SELECT " +
  	        "cir.check_in_record_id, cir.customer_id, cir.nursing_level_id, cir.user_id, " +
  	        "cir.check_in_time, cir.end_time, cir.state, " +
  	        
  	        "c.customer_id as c_customer_id, c.name as c_name, c.type as c_type, " +
  	        "c.gender as c_gender, c.identity as c_identity, " +
  	        "c.blood_type as c_blood_type, c.tel as c_tel, " +
  	        
  	        "f.family_id as f_family_id, f.customer_id as f_customer_id, " +
  	        "f.name as f_name, f.relation as f_relation, f.tel as f_tel,f.state as f_state, " +
  	        
  	        "br.bed_record_id as br_bed_record_id, br.bed_id as br_bed_id, " +
  	        "br.check_in_record_id as br_check_in_record_id, br.state as br_state, " +
  	        "br.start_time as br_start_time, br.end_time as br_end_time, " +
  	        
  	        "b.bed_id as b_bed_id, b.room_id as b_room_id, b.bed_number as b_bed_number, b.available as b_available, " +
  	        "r.room_id as r_room_id, r.room_number as r_room_number, r.building_number as r_building_number, " +
  	        "r.floor as r_floor, r.bed_count as r_bed_count, " +
  	        "nl.nursing_level_id as nl_nursing_level_id, nl.name as nl_name, nl.state as nl_state, " +
  	        "TIMESTAMPDIFF(YEAR, STR_TO_DATE(SUBSTRING(c.identity, 7, 8), '%Y%m%d'), CURDATE()) as age " +
  	        "FROM t_customer c " +
  	        "JOIN t_check_in_record cir ON c.customer_id = cir.customer_id " +
  	        "LEFT JOIN t_family f ON c.customer_id = f.customer_id AND f.state=1 " +
  	        "LEFT JOIN t_bed_record br ON cir.check_in_record_id = br.check_in_record_id AND br.state = 1 " +
  	        "LEFT JOIN t_bed b ON br.bed_id = b.bed_id " +
  	        "LEFT JOIN t_room r ON b.room_id = r.room_id " +
  	        "LEFT JOIN t_nursing_level nl ON nl.nursing_level_id = cir.nursing_level_id " +
  	        "WHERE c.type = '1' AND cir.state = 1 AND cir.user_id IS NULL " +
  	        "LIMIT #{offset}, #{pageSize}")
  	@Results({
  	    // 主对象映射
  	    @Result(property = "age", column = "age"),
  	    
  	    // CheckInRecord映射
  	    @Result(property = "checkInRecord.checkInRecordId", column = "check_in_record_id"),
  	    @Result(property = "checkInRecord.customerId", column = "customer_id"),
  	    @Result(property = "checkInRecord.nursingLevelId", column = "nursing_level_id"),
  	    @Result(property = "checkInRecord.userId", column = "user_id"),
  	    @Result(property = "checkInRecord.checkInTime", column = "check_in_time"),
  	    @Result(property = "checkInRecord.endTime", column = "end_time"),
  	    @Result(property = "checkInRecord.state", column = "state"),
  	    
  	    // Customer映射
  	    @Result(property = "customer.customerId", column = "c_customer_id"),
  	    @Result(property = "customer.name", column = "c_name"),
  	    @Result(property = "customer.type", column = "c_type"),
  	    @Result(property = "customer.gender", column = "c_gender"),
  	    @Result(property = "customer.identity", column = "c_identity"),
  	    @Result(property = "customer.bloodType", column = "c_blood_type"),
  	    @Result(property = "customer.tel", column = "c_tel"),
  	    
  	    // Family映射
  	    @Result(property = "family.familyId", column = "f_family_id"),
  	    @Result(property = "family.customerId", column = "f_customer_id"),
  	    @Result(property = "family.name", column = "f_name"),
  	    @Result(property = "family.relation", column = "f_relation"),
  	    @Result(property = "family.tel", column = "f_tel"),
  	  @Result(property = "family.state", column = "f_state"),
  	    
  	    // BedRecord映射
  	    @Result(property = "bedRecord.bedRecordId", column = "br_bed_record_id"),
  	    @Result(property = "bedRecord.bedId", column = "br_bed_id"),
  	    @Result(property = "bedRecord.checkInRecordId", column = "br_check_in_record_id"),
  	    @Result(property = "bedRecord.state", column = "br_state"),
  	    @Result(property = "bedRecord.startTime", column = "br_start_time"),
  	    @Result(property = "bedRecord.endTime", column = "br_end_time"),
  	    
  	    // Bed映射
  	    @Result(property = "bed.bedId", column = "b_bed_id"),
  	    @Result(property = "bed.roomId", column = "b_room_id"),
  	    @Result(property = "bed.bedNumber", column = "b_bed_number"),
  	    @Result(property = "bed.available", column = "b_available"),
  	    
  	    // Room映射
  	    @Result(property = "room.roomId", column = "r_room_id"),
  	    @Result(property = "room.roomNumber", column = "r_room_number"),
  	    @Result(property = "room.buildingNumber", column = "r_building_number"),
  	    @Result(property = "room.floor", column = "r_floor"),
  	    @Result(property = "room.bedCount", column = "r_bed_count"),
  	    
  	 // NursingLevel映射
  	    @Result(property = "nursingLevel.nursingLevelId", column = "nl_nursing_level_id"),
  	    @Result(property = "nursingLevel.name", column = "nl_name"),
  	    @Result(property = "nursingLevel.state", column = "nl_state")
  	})
  	List<CustCheckInNurseDTO> showUnCust(long offset,long pageSize);
  	@Select("SELECT COUNT(*) FROM t_customer c " +
  	        "JOIN t_check_in_record cir ON c.customer_id = cir.customer_id " +
  	        "LEFT JOIN t_family f ON c.customer_id = f.customer_id AND f.state = 1 " +
  	        "LEFT JOIN t_bed_record br ON cir.check_in_record_id = br.check_in_record_id AND br.state = 1 " +
  	        "LEFT JOIN t_bed b ON br.bed_id = b.bed_id " +
  	        "LEFT JOIN t_room r ON b.room_id = r.room_id " +
  	        "LEFT JOIN t_nursing_level nl ON nl.nursing_level_id = cir.nursing_level_id " +
  	        "WHERE c.type = '1' AND cir.state = 1 AND cir.user_id IS NULL")
  	long countUnCustomers();
  	
  	
  	/*多条件组合查询没有护工的护理客户信息*/
	@Select("<script>" +
	        "SELECT " +
	        "cir.check_in_record_id, cir.customer_id, cir.nursing_level_id, cir.user_id, " +
	        "cir.check_in_time, cir.end_time, cir.state, " +
	        
	        "c.customer_id as c_customer_id, c.name as c_name, c.type as c_type, " +
	        "c.gender as c_gender, c.identity as c_identity, " +
	        "c.blood_type as c_blood_type, c.tel as c_tel, " +
	        
	        "f.family_id as f_family_id, f.customer_id as f_customer_id, " +
	        "f.name as f_name, f.relation as f_relation, f.tel as f_tel,f.state as f_state, " +
	        
	        "br.bed_record_id as br_bed_record_id, br.bed_id as br_bed_id, " +
	        "br.check_in_record_id as br_check_in_record_id, br.state as br_state, " +
	        "br.start_time as br_start_time, br.end_time as br_end_time, " +
	        
	        "b.bed_id as b_bed_id, b.room_id as b_room_id, b.bed_number as b_bed_number, b.available as b_available, " +
	        "r.room_id as r_room_id, r.room_number as r_room_number, r.building_number as r_building_number, " +
	        "r.floor as r_floor, r.bed_count as r_bed_count, " +
	        "nl.nursing_level_id as nl_nursing_level_id, nl.name as nl_name, nl.state as nl_state, " +
	        "TIMESTAMPDIFF(YEAR, STR_TO_DATE(SUBSTRING(c.identity, 7, 8), '%Y%m%d'), CURDATE()) as age " +
	        "FROM t_customer c " +
	        "JOIN t_check_in_record cir ON c.customer_id = cir.customer_id " +
	        "LEFT JOIN t_family f ON c.customer_id = f.customer_id AND f.state=1 " +
	        "LEFT JOIN t_bed_record br ON cir.check_in_record_id = br.check_in_record_id AND br.state = 1 " +
	        "LEFT JOIN t_bed b ON br.bed_id = b.bed_id " +
	        "LEFT JOIN t_room r ON b.room_id = r.room_id " +
	        "LEFT JOIN t_nursing_level nl ON nl.nursing_level_id = cir.nursing_level_id " +
	        "WHERE cir.state = 1 AND c.type=1 AND cir.user_id IS NULL " +
	        "<if test='name != null and name != \"\"'>" +
	        "   AND c.name LIKE CONCAT('%', #{name}, '%') " +
	        "</if>" +
	        "<if test='checkInTime != null and checkInTime != \"\"'>" +
	        "   AND cir.check_in_time >= #{checkInTime} " +
	        "</if>" +
	        "<if test='nursingLevelId != null and nursingLevelId != \"\"'>" +
	        "   AND cir.nursing_level_id= #{nursingLevelId} " +
	        "</if>" +
	        "LIMIT #{offset}, #{pageSize}" +
	        "</script>")
	@Results({
	    // 主对象映射
	    @Result(property = "age", column = "age"),
	    
	    // CheckInRecord映射
	    @Result(property = "checkInRecord.checkInRecordId", column = "check_in_record_id"),
	    @Result(property = "checkInRecord.customerId", column = "customer_id"),
	    @Result(property = "checkInRecord.nursingLevelId", column = "nursing_level_id"),
	    @Result(property = "checkInRecord.userId", column = "user_id"),
	    @Result(property = "checkInRecord.checkInTime", column = "check_in_time"),
	    @Result(property = "checkInRecord.endTime", column = "end_time"),
	    @Result(property = "checkInRecord.state", column = "state"),
	    
	    // Customer映射
	    @Result(property = "customer.customerId", column = "c_customer_id"),
	    @Result(property = "customer.name", column = "c_name"),
	    @Result(property = "customer.type", column = "c_type"),
	    @Result(property = "customer.gender", column = "c_gender"),
	    @Result(property = "customer.identity", column = "c_identity"),
	    @Result(property = "customer.bloodType", column = "c_blood_type"),
	    @Result(property = "customer.tel", column = "c_tel"),
	    
	    // Family映射
	    @Result(property = "family.familyId", column = "f_family_id"),
	    @Result(property = "family.customerId", column = "f_customer_id"),
	    @Result(property = "family.name", column = "f_name"),
	    @Result(property = "family.relation", column = "f_relation"),
	    @Result(property = "family.tel", column = "f_tel"),
	    @Result(property = "family.state", column = "f_state"),
	    
	    // BedRecord映射
	    @Result(property = "bedRecord.bedRecordId", column = "br_bed_record_id"),
	    @Result(property = "bedRecord.bedId", column = "br_bed_id"),
	    @Result(property = "bedRecord.checkInRecordId", column = "br_check_in_record_id"),
	    @Result(property = "bedRecord.state", column = "br_state"),
	    @Result(property = "bedRecord.startTime", column = "br_start_time"),
	    @Result(property = "bedRecord.endTime", column = "br_end_time"),
	    
	    // Bed映射
	    @Result(property = "bed.bedId", column = "b_bed_id"),
	    @Result(property = "bed.roomId", column = "b_room_id"),
	    @Result(property = "bed.bedNumber", column = "b_bed_number"),
	    @Result(property = "bed.available", column = "b_available"),
	    
	    // Room映射
	    @Result(property = "room.roomId", column = "r_room_id"),
	    @Result(property = "room.roomNumber", column = "r_room_number"),
	    @Result(property = "room.buildingNumber", column = "r_building_number"),
	    @Result(property = "room.floor", column = "r_floor"),
	    @Result(property = "room.bedCount", column = "r_bed_count"),
	    
	    // NursingLevel映射 - 条件性映射
	    @Result(property = "nursingLevel.nursingLevelId", column = "nl_nursing_level_id"),
	    @Result(property = "nursingLevel.name", column = "nl_name"),
	    @Result(property = "nursingLevel.state", column = "nl_state")
	})
	List<CustCheckInNurseDTO> searchUnCust(@Param("name") String name, 
	                                   @Param("checkInTime") String checkInTime,
	                                   @Param("nursingLevelId") Integer nursingLevelId,
	                                   @Param("offset") long offset, 
	                                   @Param("pageSize") long pageSize);

	@Select("<script>" +
	        "SELECT COUNT(*) FROM t_customer c " +
	        "JOIN t_check_in_record cir ON c.customer_id = cir.customer_id " +
	        "LEFT JOIN t_family f ON c.customer_id = f.customer_id AND f.state = 1 " +
	        "LEFT JOIN t_bed_record br ON cir.check_in_record_id = br.check_in_record_id AND br.state = 1 " +
	        "LEFT JOIN t_bed b ON br.bed_id = b.bed_id " +
	        "LEFT JOIN t_room r ON b.room_id = r.room_id " +
	        "LEFT JOIN t_nursing_level nl ON nl.nursing_level_id = cir.nursing_level_id " +
	        "WHERE cir.state = 1 AND c.type = 1 AND cir.user_id IS NULL " +
	        "<if test='name != null and name != \"\"'>" +
	        "   AND c.name LIKE CONCAT('%', #{name}, '%') " +
	        "</if>" +
	        "<if test='checkInTime != null and checkInTime != \"\"'>" +
	        "   AND cir.check_in_time >= #{checkInTime} " +
	        "</if>" +
	        "<if test='nursingLevelId != null'>" +
	        "   AND cir.nursing_level_id = #{nursingLevelId} " +
	        "</if>" +
	        "</script>")
	long countSearchUnCust(@Param("name") String name, 
	                     @Param("checkInTime") String checkInTime,
	                     @Param("nursingLevelId") Integer nursingLevelId);

	




	/*=============护工模块 显示该护工下的老人 对应请求路径"/user/showUserCust"=========*/
	List<CustDailyNursingDTO> findUserCust(Integer userId);
	//客户管理模块显示老人信息（其实是增加了身份证号，入住时间以及到期时间这三项内容）
	List<CustNursingManageDTO> findUserCustManage(Integer userId);
	//根据老人姓名模糊搜索
	List<CustDailyNursingDTO> findUserCustByName(Integer userId,String name);
	//客户管理模块根据老人姓名模糊搜索（其实是增加了身份证号，入住时间以及到期时间这三项内容）
	List<CustNursingManageDTO> findUserCustManageByName(Integer userId,String name);


}
