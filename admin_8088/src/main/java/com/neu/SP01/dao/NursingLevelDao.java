package com.neu.SP01.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.neu.SP01.po.NursingLevel;
import com.neu.SP01.po.NursingProject;

/**
 * 对应t_nursing_level
 */
@Mapper
public interface NursingLevelDao {
	/*分页搜索护理级别*/
    @Select("<script>" +
            "SELECT * FROM t_nursing_level " +
            "WHERE 1=1 " +
            "<if test='name != null and name != \"\"'> " +
            "   AND name LIKE CONCAT('%', #{name}, '%') " +
            "</if> " +
            "<if test='state != null'> " +
            "   AND state = #{state} " +
            "</if> " +
            "LIMIT #{offset}, #{pageSize}" +
            "</script>")
    List<NursingLevel> searchLevels(
            @Param("name") String name,
            @Param("state") Integer state,
            @Param("offset") Long offset,
            @Param("pageSize") Long pageSize);

    /*统计符合条件的护理级别数量*/
    @Select("<script>" +
            "SELECT COUNT(*) FROM t_nursing_level " +
            "WHERE 1=1 " +
            "<if test='name != null and name != \"\"'> " +
            "   AND name LIKE CONCAT('%', #{name}, '%') " +
            "</if> " +
            "<if test='state != null'> " +
            "   AND state = #{state} " +
            "</if>" +
            "</script>")
    Long countSearchLevels(
            @Param("name") String name,
            @Param("state") Integer state);
    
    //新增护理级别
    @Insert("INSERT INTO t_nursing_level(name, state) " +
            "VALUES(#{name}, #{state})")
    @Options(useGeneratedKeys = true, keyProperty = "nursingLevelId", keyColumn = "nursingLevel_id")
    int insert(NursingLevel nursingLevel);
    
    //删除护理级别
    @Update("update t_nursing_level set state=0 where nursing_level_id=#{nursingLevelId}")
    int deleteNursingLevel(@Param("nursingLevelId") Integer nursingLevelId);
    
    // 查询该级别下有的项目
    @Select("SELECT p.* FROM t_nursing_project p " +
            "JOIN t_nursing_level_project lp ON p.nursing_project_id = lp.nursing_project_id " +
            "WHERE lp.nursing_level_id = #{nursingLevelId} " +
            "AND lp.state = 1 " +
            "AND p.state = 1")
    List<NursingProject> showNursingPro(@Param("nursingLevelId") Integer nursingLevelId);
    
    //查询该级别下没有的项目 
    @Select("SELECT * FROM t_nursing_project " +
            "WHERE state = 1 " +
            "AND nursing_project_id NOT IN (" +
            "   SELECT nursing_project_id FROM t_nursing_level_project " +
            "   WHERE nursing_level_id = #{nursingLevelId} AND state = 1" +
            ")")
    List<NursingProject> findAvailableProjectsForLevel(@Param("nursingLevelId") Integer nursingLevelId);
    
    //修改护理级别
    @Update("UPDATE t_nursing_level " +
            "SET name = #{name}, " +
            "state = #{state} " +
            "WHERE nursing_level_id = #{nursingLevelId}")
    int updateNursingLevel(NursingLevel nursingLevel);
    // 按照id查询
    @Select("select *  from t_nursing_level where nursing_level_id=#{nursingLevelId}")
    NursingLevel selectById(Integer nursingLevelId);
    
 // 按照id查询
    @Select("select *  from t_nursing_level where state=1")
    List<NursingLevel> showOk();
    
    
    /*---------------------------------------------------------*/
    
    
    // 按照级别名称查询(应该就返回一个查询到的护理级别吧？)
    @Select("select *  from yyzx_st.t_nursing_level where name=#{name}")
    NursingLevel findNursingLevelByName(String name);

    //修改护理级别的状态
    @Update("update yyzx_st.t_nursing_level set state=#{state} where nursing_level_id=#{NursingLevelId}")
    void updateByState(NursingLevel nl);

 

}
