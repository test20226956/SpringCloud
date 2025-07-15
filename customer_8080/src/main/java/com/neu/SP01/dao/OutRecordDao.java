package com.neu.SP01.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.neu.SP01.po.CustOutRecordDTO;
import com.neu.SP01.po.OutDetailDTO;
import com.neu.SP01.po.OutRecord;
import com.neu.SP01.po.OutRecordWithName;

/**
 * 对应t_out_record
 */
@Mapper
public interface OutRecordDao {
    /*管理员模块*/
	
	/*分页查询外出申请记录*/
	@Select("SELECT r.*, c.name AS customerName ,c.gender,c.identity,u.user_name AS nurseName " +
            "FROM t_out_record r " +
            "LEFT JOIN t_customer c ON r.customer_id = c.customer_id " +
            "LEFT JOIN t_user u ON r.nurse_id = u.user_id " +
            "ORDER BY r.apply_time DESC " +
            "LIMIT #{offset}, #{pageSize}")
    List<OutRecordWithName> showGoOut(@Param("offset") long offset, 
                                            @Param("pageSize") long pageSize);
	/*查询外出退住申请总记录数*/
	@Select("SELECT COUNT(*) FROM t_out_record r " +
	        "LEFT JOIN t_customer c ON r.customer_id = c.customer_id " +
	        "LEFT JOIN t_user u ON r.nurse_id = u.user_id")
	long countGoOut();
	
	/*分页多条件查询*/
	@Select("<script>" +
            "SELECT r.*, c.name AS customerName ,c.gender,c.identity,u.user_name AS nurseName " +
            "FROM t_out_record r " +
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
    List<OutRecordWithName> queryByConditions(
            @Param("customerName") String customerName,
            @Param("state") Integer state,
            @Param("applyTime") String applyTime,
            @Param("offset") long offset,
            @Param("pageSize") long pageSize);

    /*多条件查询总数*/
    @Select("<script>" +
            "SELECT COUNT(*) " +
            "FROM t_out_record r " +
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
    @Update("UPDATE t_out_record " +
            "SET state = #{state}, " +
            "admin_id = #{adminId}, " +
            "examine_time = #{examineTime} " +
            "WHERE out_record_id = #{outRecordId}")
    int approveOut(@Param("outRecordId") Integer outRecordId,
                       @Param("state") Integer state,
                       @Param("adminId") Integer adminId,
                       @Param("examineTime") String examineTime);
    
    /*根据退住记录ID获取客户ID*/
    @Select("SELECT customer_id FROM t_out_record WHERE out_record_id = #{outRecordId}")
    Integer findCustomerIdByOutId(@Param("outRecordId") Integer outRecordId);
    
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
            "FROM t_out_record cor " +
            "JOIN t_customer c ON cor.customer_id = c.customer_id " +
            "JOIN t_check_in_record cir ON c.customer_id = cir.customer_id " +
            "JOIN t_bed_record br ON cir.check_in_record_id = br.check_in_record_id " +
            "JOIN t_bed b ON br.bed_id = b.bed_id " +
            "JOIN t_room r ON b.room_id = r.room_id " +
            "JOIN t_user u ON u.user_id = cor.nurse_id " +
            "WHERE cor.out_record_id = #{outRecordId} " +
            "AND cir.state = 1 "+
            "AND br.state=1 ")
    OutDetailDTO getOutDetailById(@Param("outRecordId") Integer outRecordId);
	
    
    /*
    护工模块下的客户管理外出记录
     */
    //添加用户的外出记录
    @Insert("insert into yyzx_st.t_out_record values (null,#{customerId},#{applyTime},#{examineTime},0,#{adminId},#{reason},#{outTime},#{expectedReturnTime},#{nurseId},null)")
    void addOutRecord(OutRecord or);
    //根据老人查找对应的外出申请(外出申请功能页面中点击老人的申请记录)（多表查询 t_out_record t_customer）
    List<CustOutRecordDTO> findOutRecordByCustomerId(Integer customerId);
    //给用户添加回院时间
    @Update("update yyzx_st.t_out_record set actual_return_time=#{actualReturnTime} where out_record_id=#{outRecordId}")
    void AddActualReturnTime(Integer outRecordId, String actualReturnTime);
    @Select("select * from yyzx_st.t_out_record where out_record_id=#{outRecordId}")
    OutRecord findOutRecordById(Integer outRecordId);

    //根据申请时间查询老人的外出申请记录
    List<CustOutRecordDTO> findOutRecordByTime(Integer customerId,String outTime);

}
