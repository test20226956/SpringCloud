package com.neu.SP01.dao;

import com.neu.SP01.po.NursingLevel;
import com.neu.SP01.po.NursingProject;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 对应 t_nursing_project
 */
@Mapper
public interface NursingProjectDao {
    
	/*分页搜索护理项目*/
    @Select("<script>" +
            "SELECT * FROM t_nursing_project " +
            "WHERE 1=1 " +
            "<if test='name != null and name != \"\"'> " +
            "   AND name LIKE CONCAT('%', #{name}, '%') " +
            "</if> " +
            "<if test='state != null'> " +
            "   AND state = #{state} " +
            "</if> " +
            "LIMIT #{offset}, #{pageSize}" +
            "</script>")
    List<NursingProject> searchProjects(
            @Param("name") String name,
            @Param("state") Integer state,
            @Param("offset") Long offset,
            @Param("pageSize") Long pageSize);

    /*统计符合条件的护理级别数量*/
    @Select("<script>" +
            "SELECT COUNT(*) FROM t_nursing_project " +
            "WHERE 1=1 " +
            "<if test='name != null and name != \"\"'> " +
            "   AND name LIKE CONCAT('%', #{name}, '%') " +
            "</if> " +
            "<if test='state != null'> " +
            "   AND state = #{state} " +
            "</if>" +
            "</script>")
    Long countSearchProjects(
            @Param("name") String name,
            @Param("state") Integer state);
    
    //添加护理项目
    @Insert("INSERT INTO t_nursing_project(state, time, name, price, period, description) " +
            "VALUES(#{state}, #{time}, #{name}, #{price}, #{period}, #{description})")
    @Options(useGeneratedKeys = true, keyProperty = "nursingProjectId")
    int insert(NursingProject nursingProject);
    
 // 动态更新项目信息
    @Update("UPDATE t_nursing_project set state = #{state},time = #{time},"
    		+ "name = #{name},price = #{price},period = #{period},description = #{description} "
    		+ "WHERE nursing_project_id = #{nursingProjectId}")
    int updateSelective(NursingProject nursingProject);
    
    // 根据ID查询原始数据
    @Select("SELECT * FROM t_nursing_project WHERE nursing_project_id = #{projectId}")
    NursingProject selectById(Integer projectId);
    
    
    //删除项目
    @Update("update t_nursing_project set state=-1 where nursing_project_id=#{nursingProjectId}")
    int deleteNursingProject(Integer nursingProjectId);
    
    //查客户没有的项目
    @Select("<script>" +
            "SELECT DISTINCT np.nursing_project_id, np.name as name, np.description, np.price " +
            "FROM t_nursing_project np " +
            "JOIN t_nursing_level_project nlp ON np.nursing_project_id = nlp.nursing_project_id " +
            "WHERE nlp.nursing_level_id = #{nursingLevelId} " +
            "AND nlp.state = 1 " +
            "AND np.state = 1 " +
            "AND np.nursing_project_id NOT IN (" +
            "   SELECT ns.nursing_project_id " +
            "   FROM t_nursing_service ns " +
            "   WHERE ns.customer_id = #{customerId} " +
            "   AND ns.state =1" +
            ") " +
            "</script>")
    @Results({
        @Result(property = "nursingProjectId", column = "nursing_project_id"),
        @Result(property = "name", column = "name"),
        @Result(property = "description", column = "description"),
        @Result(property = "price", column = "price")
    })
    List<NursingProject> findUnpurchasedProjects(
            @Param("customerId") Integer customerId,
            @Param("nursingLevelId") Integer nursingLevelId);
    //搜索客户没有的项目
    @Select("<script>" +
            "SELECT DISTINCT np.nursing_project_id, np.name, np.description, np.price " +
            "FROM t_nursing_project np " +
            "JOIN t_nursing_level_project nlp ON np.nursing_project_id = nlp.nursing_project_id " +
            "WHERE nlp.nursing_level_id = #{nursingLevelId} " +
            "AND nlp.state = 1 " +
            "AND np.state = 1 " +
            "AND np.nursing_project_id NOT IN (" +
            "   SELECT ns.nursing_project_id " +
            "   FROM t_nursing_service ns " +
            "   WHERE ns.customer_id = #{customerId} " +
            "   AND ns.state = 1" +
            ")" +
            "<if test='name != null and name != &quot;&quot;'>" +  // 修改此处
            "   AND np.name LIKE CONCAT('%', #{name}, '%')" +
            "</if>" +
            "</script>")
    @Results({
        @Result(property = "nursingProjectId", column = "nursing_project_id"),
        @Result(property = "name", column = "name"),
        @Result(property = "description", column = "description"),
        @Result(property = "price", column = "price")
    })
    List<NursingProject> searchUnpurchasedProjects(
            @Param("customerId") Integer customerId,
            @Param("nursingLevelId") Integer nursingLevelId,
            @Param("name") String name);

}
