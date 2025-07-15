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

import com.neu.SP01.po.NursingService;
import com.neu.SP01.po.NursingServiceDTO;
import com.neu.SP01.po.NursingServiceDailyDTO;
import com.neu.SP01.po.PopularNursingProject;

/**
 * 对应表t_nursing_service
 */
@Mapper
public interface NursingServiceDao {
    //显示用户所有的护理项目(对应原型服务关注 所以多表t_nursing_service->t_nursing_project
    
    
    //删除护理服务
    @Update("update t_nursing_service set state=0 where customer_id=#{customerId} and state=1")
    int deleteNursingService(Integer customerId);
    
    //批量添加
    @Insert({
        "<script>",
        "INSERT INTO t_nursing_service (",
        "   customer_id, nursing_level_id, nursing_project_id,",
        "   amount, purchase_time, end_time, state",
        ") VALUES ",
        "<foreach collection='list' item='item' separator=','>",
        "   (",
        "       #{item.customerId}, #{item.nursingLevelId}, #{item.nursingProjectId},",
        "       #{item.amount}, #{item.purchaseTime}, #{item.endTime}, 1",
        "   )",
        "</foreach>",
        "</script>"
    })
    int batchInsertNursingServices(@Param("list") List<NursingService> nursingServices);
    
 // 所以返回要有项目信息还要有对应的服务信息 暂时先void)
    @Select("SELECT ns.*, np.nursing_project_id, np.name as project_name, np.state as project_state, " +
            "np.time as project_time, np.price, np.period, np.description " +
            "FROM t_nursing_service ns " +
            "LEFT JOIN t_nursing_project np ON ns.nursing_project_id = np.nursing_project_id " +
            "WHERE ns.customer_id = #{customerId} AND ns.state IN (-1,1) ")
    @Results({
        // 护理服务映射
        @Result(property = "nursingService.nursingServiceId", column = "nursing_service_id"),
        @Result(property = "nursingService.customerId", column = "customer_id"),
        @Result(property = "nursingService.nursingLevelId", column = "nursing_level_id"),
        @Result(property = "nursingService.nursingProjectId", column = "nursing_project_id"),
        @Result(property = "nursingService.amount", column = "amount"),
        @Result(property = "nursingService.purchaseTime", column = "purchase_time"),
        @Result(property = "nursingService.endTime", column = "end_time"),
        
        // 护理项目映射
        @Result(property = "nursingProject.nursingProjectId", column = "nursing_project_id"),
        @Result(property = "nursingProject.state", column = "project_state"),
        @Result(property = "nursingProject.time", column = "project_time"),
        @Result(property = "nursingProject.name", column = "project_name"),
        @Result(property = "nursingProject.price", column = "price"),
        @Result(property = "nursingProject.period", column = "period"),
        @Result(property = "nursingProject.description", column = "description")
    })
    List<NursingServiceDTO> findNursingServiceProjectByCustomerId(@Param("customerId") Integer customerId);
 // 新增：更新过期服务状态
    @Update("UPDATE t_nursing_service " +
            "SET state = -1 " +
            "WHERE state = 1 AND end_time < #{currentDate}")
    int updateExpiredServices(@Param("currentDate") String currentDate);
    
  //给客户的护理项目续费(其实对应操作是修改数量和到期时间)
    @Update("UPDATE t_nursing_service " +
            "SET amount = amount + #{amount}, " +
            "end_time = #{endTime} " +
            "WHERE nursing_service_id = #{nursingServiceId}")
    Integer updateNursingServiceRenewal(
            @Param("nursingServiceId") Integer nursingServiceId,
            @Param("amount") Integer amount,
            @Param("endTime") String endTime);
    
  //给客户的护理项目移除
    @Update("UPDATE t_nursing_service set state=0 WHERE nursing_service_id = #{nursingServiceId} ")
    Integer deleteNursingServiceById(@Param("nursingServiceId") Integer nursingServiceId);
    
 // 检查是否存在相同客户和项目的无效记录(state=-1)
    @Select("SELECT COUNT(*) FROM t_nursing_service " +
            "WHERE customer_id = #{customerId} " +
            "AND nursing_project_id = #{nursingProjectId} " +
            "AND state = -1")
    int countInactiveService(@Param("customerId") Integer customerId, 
                           @Param("nursingProjectId") Integer nursingProjectId);

    // 更新无效记录状态为0
    @Update("UPDATE t_nursing_service SET state = 0 " +
            "WHERE customer_id = #{customerId} " +
            "AND nursing_project_id = #{nursingProjectId} " +
            "AND state = -1")
    int reactivateOldService(@Param("customerId") Integer customerId,
                           @Param("nursingProjectId") Integer nursingProjectId);

    // 新增护理服务记录
    @Insert("INSERT INTO t_nursing_service(" +
            "customer_id, nursing_level_id, nursing_project_id, " +
            "amount, purchase_time, end_time, state) " +
            "VALUES(#{customerId}, #{nursingLevelId}, #{nursingProjectId}, " +
            "#{amount}, #{purchaseTime}, #{endTime}, 1)")
    @Options(useGeneratedKeys = true, keyProperty = "nursingServiceId")
    int insertNewService(NursingService nursingService);
    
 // 更新无效记录状态为0
    @Update("UPDATE t_nursing_service SET state = 0 " +
            "WHERE customer_id = #{customerId} " +
            "AND state IN (-1,1)")
    Integer detectService(@Param("customerId") Integer customerId);
    
    //统计热门项目
    @Select("SELECT p.name AS nursingPro, COUNT(*) AS count " +
            "FROM t_nursing_service s " +
            "JOIN t_nursing_project p ON s.nursing_project_id = p.nursing_project_id " +
            "WHERE s.purchase_time >= DATE_SUB(CURDATE(), INTERVAL 7 DAY) " +
            "GROUP BY s.nursing_project_id " +
            "ORDER BY count DESC " +
            "LIMIT #{limit}")
    List<PopularNursingProject> findPopularProjectsLast7Days(@Param("limit") Integer limit);


    /*======对应原型护工 日常护理 显示用户的护理服务=====*/
    List<NursingServiceDailyDTO> findNursingServiceByCustomerId(Integer customerId);

    //按项目名字搜索用户的持有的护理服务
    List<NursingServiceDailyDTO> findNursingServiceByName(@Param("customerId")Integer customerId,@Param("name")String name);
    //对应原型护工 日常护理 添加用户护理记录，相应的对应护理服务的数量也应该减少
    @Update("UPDATE yyzx_st.t_nursing_service SET amount = amount - #{count} WHERE nursing_service_id = #{nursingServiceId}")
    void updateNursingServiceAmount(Integer count,Integer nursingServiceId);
}
