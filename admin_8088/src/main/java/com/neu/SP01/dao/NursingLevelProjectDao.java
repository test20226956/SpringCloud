package com.neu.SP01.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface NursingLevelProjectDao {
	
	//删除护理级别
	@Update("UPDATE t_nursing_level_project " +
            "SET state = 0 " +
            "WHERE nursing_level_id = #{nursingLevelId}")
    int updateStateByLevelId(@Param("nursingLevelId") Integer nursingLevelId);
	
	// 查询该级别下现有的有效项目ID列表
    @Select("SELECT nursing_project_id FROM t_nursing_level_project " +
            "WHERE nursing_level_id = #{nursingLevelId} AND state = 1")
    List<Integer> findExistingProjectIds(@Param("nursingLevelId") Integer nursingLevelId);
    
    //激活
    @Update("UPDATE t_nursing_level_project SET state = 1 " +
            "WHERE nursing_level_id = #{levelId} AND nursing_project_id = #{projectId}")
    int activateRelation(@Param("levelId") Integer levelId, 
                       @Param("projectId") Integer projectId);
    //插入新的
    @Insert("INSERT INTO t_nursing_level_project(nursing_level_id, nursing_project_id, state) " +
            "VALUES(#{levelId}, #{projectId}, 1)")
    int insertRelation(@Param("levelId") Integer levelId, 
                      @Param("projectId") Integer projectId);
    //寻找
    @Select("SELECT 1 FROM t_nursing_level_project " +
            "WHERE nursing_level_id = #{levelId} AND nursing_project_id = #{projectId} " +
            "LIMIT 1")
    Integer existsRelation(@Param("levelId") Integer levelId, 
                         @Param("projectId") Integer projectId);
    
    // 逻辑删除关联（设置state=0）
    @Update("UPDATE t_nursing_level_project SET state = 0 " +
            "WHERE nursing_level_id = #{levelId} AND nursing_project_id = #{projectId}")
    int disableRelation(@Param("levelId") Integer levelId, 
                       @Param("projectId") Integer projectId);
    
    // 批量逻辑删除
    @Update({"<script>",
             "UPDATE t_nursing_level_project SET state = 0",
             "WHERE nursing_level_id = #{levelId}",
             "AND nursing_project_id IN",
             "<foreach collection='projectIds' item='id' open='(' separator=',' close=')'>",
               "#{id}",
             "</foreach>",
             "</script>"})
    int batchDisableRelations(@Param("levelId") Integer levelId,
                             @Param("projectIds") List<Integer> projectIds);
    //根据项目ID删除
    @Update("UPDATE t_nursing_level_project SET state = 0 " +
            "WHERE nursing_project_id = #{projectId} AND state = 1")
    int disableRelationsByProjectId(Integer projectId);
	
}
