package com.neu.SP01.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.neu.SP01.po.CustCheckInDietDTO;
import com.neu.SP01.po.CustomerDiet;

@Mapper
public interface CustomerDietDao {

    /*添加客户膳食配置*/
    @Insert("INSERT INTO t_customer_diet (customer_id,state) VALUES(#{customerId},1)")
    Integer addCustomerDiet(Integer customerId);

    /*多条件组合查询客户膳食信息*/
    @Select("<script>" +
            "SELECT " +
            "cir.check_in_record_id, cir.customer_id, cir.nursing_level_id, cir.user_id, " +
            "cir.check_in_time, cir.end_time, cir.state, " +
            
            "c.customer_id as c_customer_id, c.name as c_name, c.type as c_type, " +
            "c.gender as c_gender, c.identity as c_identity, " +
            "c.blood_type as c_blood_type, c.tel as c_tel, " +
            
            "f.family_id as f_family_id, f.customer_id as f_customer_id, " +
            "f.name as f_name, f.relation as f_relation, f.tel as f_tel, " +
            
            "br.bed_record_id as br_bed_record_id, br.bed_id as br_bed_id, " +
            "br.check_in_record_id as br_check_in_record_id, br.state as br_state, " +
            "br.start_time as br_start_time, br.end_time as br_end_time, " +
            
            "b.bed_id as b_bed_id, b.room_id as b_room_id, b.bed_number as b_bed_number, b.available as b_available, " +
            "r.room_id as r_room_id, r.room_number as r_room_number, r.building_number as r_building_number, " +
            "r.floor as r_floor, r.bed_count as r_bed_count, " +
            
            "cd.customer_diet_id as cd_customer_diet_id, cd.customer_id as cd_customer_id, " +
            "cd.flavor as cd_flavor, cd.restraint as cd_restraint, " +
            "cd.comment as cd_comment, cd.state as cd_state, " +
            
            "TIMESTAMPDIFF(YEAR, STR_TO_DATE(SUBSTRING(c.identity, 7, 8), '%Y%m%d'), CURDATE()) as age " +
            "FROM t_customer c " +
            "JOIN t_check_in_record cir ON c.customer_id = cir.customer_id " +
            "LEFT JOIN t_family f ON c.customer_id = f.customer_id " +
            "LEFT JOIN t_bed_record br ON cir.check_in_record_id = br.check_in_record_id AND br.state = 1 " +
            "LEFT JOIN t_bed b ON br.bed_id = b.bed_id " +
            "LEFT JOIN t_room r ON b.room_id = r.room_id " +
            "LEFT JOIN t_customer_diet cd ON c.customer_id = cd.customer_id AND cd.state = 1 " +
            "WHERE cir.state = 1 " +
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
        
        // CustomerDiet映射
        @Result(property = "customerDiet.customerDietId", column = "cd_customer_diet_id"),
        @Result(property = "customerDiet.customerId", column = "cd_customer_id"),
        @Result(property = "customerDiet.flavor", column = "cd_flavor"),
        @Result(property = "customerDiet.restraint", column = "cd_restraint"),
        @Result(property = "customerDiet.comment", column = "cd_comment"),
        @Result(property = "customerDiet.state", column = "cd_state")
    })
    List<CustCheckInDietDTO> searchCustomers(@Param("name") String name, 
                                           @Param("checkInTime") String checkInTime,
                                           @Param("offset") long offset, 
                                           @Param("pageSize") long pageSize);
    @Select("<script>" +
            "SELECT COUNT(*) FROM t_customer c " +
            "JOIN t_check_in_record cir ON c.customer_id = cir.customer_id " +
            "LEFT JOIN t_family f ON c.customer_id = f.customer_id " +
            "LEFT JOIN t_bed_record br ON cir.check_in_record_id = br.check_in_record_id AND br.state = 1 " +
            "LEFT JOIN t_bed b ON br.bed_id = b.bed_id " +
            "LEFT JOIN t_room r ON b.room_id = r.room_id " +
            "LEFT JOIN t_customer_diet cd ON c.customer_id = cd.customer_id AND cd.state = 1 " +
            "WHERE cir.state = 1 " +
            "<if test='name != null and name != \"\"'>" +
            "   AND c.name LIKE CONCAT('%', #{name}, '%') " +
            "</if>" +
            "<if test='checkInTime != null and checkInTime != \"\"'>" +
            "   AND cir.check_in_time >= #{checkInTime} " +
            "</if>" +
            "</script>")
    long countSearchCustomers(@Param("name") String name, 
                             @Param("checkInTime") String checkInTime);
    
    //编辑客户膳食配置
    @Update("update t_customer_diet set flavor=#{flavor}, restraint=#{restraint},comment=#{comment} where customer_diet_id=#{customerDietId}")
    Integer editCustDiet(CustomerDiet customerdiet);
    
  //退住改为无效
    @Update("update t_customer_diet set state=0 where customer_id=#{customerId} AND state=1")
    Integer deleteCustDiet(Integer customerId);
    
 // 根据老人ID查询口味偏好
    @Select("SELECT * FROM t_customer_diet WHERE customer_id = #{customerId} AND state=1")
    CustomerDiet getElderlyPreferences(@Param("customerId") Integer customerId);

    
}