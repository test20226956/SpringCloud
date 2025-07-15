package com.neu.SP01.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.neu.SP01.po.CustNursingRecordDTO;
import com.neu.SP01.po.DailyNursingCount;
import com.neu.SP01.po.NursingRecord;
import com.neu.SP01.po.NursingRecordDTO;

/**
 * 对应t_nursing_record
 */
@Mapper
public interface NursingRecordDao {
    //展示老人所有的护理记录（护工 健康管家 护理记录）
    List<CustNursingRecordDTO> findByCustomerId(Integer customerId);
    //删除老人的护理记录(批量删除->逻辑删除)
    void deleteByIds(@Param("ids") List<Integer> ids);
    //搜索护理记录（原型中体现的 名称（record->service->project）和护理时间 多表查 动态sql）
    List<CustNursingRecordDTO> findByNameAndTime(Integer customerId,@Param("name")String name, @Param("time")String time);

    //添加老人护理记录
    @Insert("insert into yyzx_st.t_nursing_record values (null,#{nursingServiceId},#{customerId},#{time},#{count},#{userId},1)")
    void addNursingRecord(NursingRecord nr);
    
    
    //查找护理记录详细信息  
    @Select("<script>" +
            "SELECT " +
            "  nr.nursing_record_id, nr.time, nr.nursing_service_id, nr.customer_id, " +
            "  nr.count, nr.user_id, nr.state, " +
            "  ns.nursing_service_id, ns.customer_id, ns.nursing_level_id, " +
            "  ns.nursing_project_id, ns.amount, ns.purchase_time, ns.end_time, " +
            "  np.nursing_project_id, np.name as project_name, np.description, " +
            "  u.user_id, u.user_name as nurse_name, u.tel " +
            "FROM t_nursing_record nr " +
            "LEFT JOIN t_nursing_service ns ON nr.nursing_service_id = ns.nursing_service_id " +
            "LEFT JOIN t_nursing_project np ON ns.nursing_project_id = np.nursing_project_id " +
            "LEFT JOIN t_user u ON nr.user_id = u.user_id " +
            "WHERE nr.state = 1 " +
            "<if test='customerId != null and customerId != \"\"'>" +
            "  AND nr.customer_id = #{customerId} " +
            "</if>" +
            "<if test='time != null and time != \"\"'>" +
            "  AND nr.time >= #{time} " +
            "</if>" +
            "<if test='projectName != null and projectName != \"\"'>" +
            "  AND np.name LIKE CONCAT('%', #{projectName}, '%') " +
            "</if>" +
            "ORDER BY nr.time DESC " +
            "LIMIT #{offset}, #{pageSize}" +
            "</script>")
        @Results({
            // 护理记录映射 (对应 nursingRecord 属性)
            @Result(property = "nursingRecord.nursingRecordId", column = "nursing_record_id"),
            @Result(property = "nursingRecord.time", column = "time"),
            @Result(property = "nursingRecord.nursingServiceId", column = "nursing_service_id"),
            @Result(property = "nursingRecord.customerId", column = "customer_id"),
            @Result(property = "nursingRecord.count", column = "count"),
            @Result(property = "nursingRecord.userId", column = "user_id"),
            @Result(property = "nursingRecord.state", column = "state"),
            
            // 护理服务映射 (对应 nursingService 属性)
            @Result(property = "nursingService.nursingServiceId", column = "nursing_service_id"),
            @Result(property = "nursingService.customerId", column = "customer_id"),
            @Result(property = "nursingService.nursingLevelId", column = "nursing_level_id"),
            @Result(property = "nursingService.nursingProjectId", column = "nursing_project_id"),
            @Result(property = "nursingService.amount", column = "amount"),
            @Result(property = "nursingService.purchaseTime", column = "purchase_time"),
            @Result(property = "nursingService.endTime", column = "end_time"),
            
            // 护理项目映射 (对应 nursingProject 属性)
            @Result(property = "nursingProject.nursingProjectId", column = "nursing_project_id"),
            @Result(property = "nursingProject.name", column = "project_name"),
            @Result(property = "nursingProject.description", column = "description"),
            
            // 护理人员映射 (对应 nurse 属性)
            @Result(property = "nurse.userId", column = "user_id"),
            @Result(property = "nurse.userName", column = "nurse_name"),
            @Result(property = "nurse.tel", column = "tel")
        })
        List<NursingRecordDTO> showNursingRecord(
            @Param("customerId") Integer customerId,
            @Param("time") String time,
            @Param("projectName") String projectName,
            @Param("offset") long offset,
            @Param("pageSize") long pageSize);

    @Select("<script>" +
            "SELECT COUNT(*) " +
            "FROM t_nursing_record nr " +
            "LEFT JOIN t_nursing_service ns ON nr.nursing_service_id = ns.nursing_service_id " +
            "LEFT JOIN t_nursing_project np ON ns.nursing_project_id = np.nursing_project_id " +
            "LEFT JOIN t_user u ON nr.user_id = u.user_id " +
            "WHERE nr.state = 1 " +
            "<if test='customerId != null'>" +
            "  AND nr.customer_id = #{customerId} " +
            "</if>" +
            "<if test='time != null and time != \"\"'>" +
            "  AND nr.time >= #{time} " +
            "</if>" +
            "<if test='projectName != null and projectName != \"\"'>" +
            "  AND np.name LIKE CONCAT('%', #{projectName}, '%') " +
            "</if>" +
            "</script>")
        long countSearchRecords(
            @Param("customerId") Integer customerId,
            @Param("time") String time,
            @Param("projectName") String projectName);
    
   //删除护理记录
   @Update("update t_nursing_record set state=0 where nursing_record_id=#{nursingRecordId}")
   Integer deleteNursingRecord(Integer nursingRecordId);
   
 //根据ID护理记录
   @Update("update t_nursing_record set state=0 where customer_id=#{customerId} AND state=1")
   Integer deleteOutNursingRecord(Integer customerId);

   //计数护理业务
   @Select("<script>" +
           "SELECT DATE(n.time) AS date, COUNT(*) AS nursingNum " +
           "FROM t_nursing_record n " +
           "JOIN t_check_in_record c ON n.customer_id = c.customer_id " +
           "WHERE n.state = 1 " +
           "AND DATE(n.time) >= DATE_SUB(CURDATE(), INTERVAL #{days} DAY) " +
           "<if test='nursingLevelId != null'>" +
           "   AND c.nursing_level_id = #{nursingLevelId} " +
           "</if>" +
           "GROUP BY DATE(n.time) " +
           "ORDER BY DATE(n.time) DESC" +
           "</script>")
   List<DailyNursingCount> countRecentDailyNursing(
           @Param("days") Integer days,
           @Param("nursingLevelId") Integer nursingLevelId);

    //客户端->展示老人相应护理项目下的护理记录
    List<CustNursingRecordDTO> findByNursingServiceId(Integer nursingServiceId);
}
