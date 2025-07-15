package com.neu.SP01.dao;

import com.neu.SP01.po.CustCheckInNurseDTO;
import com.neu.SP01.po.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 对应 t_user
 */
@Mapper
public interface UserDao {
    //登录
	@Select("SELECT * FROM t_user WHERE account = #{account}")
    User findUserByAccount(String account);
    /*
    用户管理
     */
    //角色列表
    @Select("select * from yyzx_st.t_user where type=#{type}")
    List<User> findUsers(Integer type);
    
    //按编号或者姓名动态查询
    List<User> findUsersByIdAndName(Integer type,Integer userId,String userName);

    //删除护工
    @Delete("delete from yyzx_st.t_user where user_id=#{userId}")
    void deleteUserById(Integer userId);
    
    /*分页查询所有普通用户(type=1)*/
    @Select("SELECT * FROM t_user WHERE type = 1 AND state=1 LIMIT #{offset}, #{pageSize}")
    List<User> findUsersByTypeWithPage(
            @Param("offset") Integer offset,
            @Param("pageSize") Integer pageSize);

    /*统计普通用户数量(type=1)*/
    @Select("SELECT COUNT(*) FROM t_user WHERE type = 1 AND state=1")
    long countUsersByType();
    
    /*多条件分页查询普通用户(type=1)*/
    @Select("<script>" +
            "SELECT * FROM t_user WHERE type = 1 AND state=1" +
            "<if test='userName != null and userName != \"\"'> " +
            "   AND user_name LIKE CONCAT('%', #{userName}, '%') " +
            "</if> " +
            "<if test='account != null and account != \"\"'> " +
            "   AND account LIKE CONCAT('%', #{account}, '%') " +
            "</if> " +
            "<if test='employmentDate != null'> " +
            "   AND employment_date >= #{employmentDate} " +
            "</if> " +
            "LIMIT #{offset}, #{pageSize}" +
            "</script>")
    List<User> findUsersByConditions(
            @Param("userName") String userName,
            @Param("account") String account,
            @Param("employmentDate") String employmentDate,
            @Param("offset") Integer offset,
            @Param("pageSize") Integer pageSize);

    /**
     * 统计符合条件用户数量
     */
    @Select("<script>" +
            "SELECT COUNT(*) FROM t_user WHERE type = 1 AND state=1" +
            "<if test='userName != null and userName != \"\"'> " +
            "   AND user_name LIKE CONCAT('%', #{userName}, '%') " +
            "</if> " +
            "<if test='account != null and account != \"\"'> " +
            "   AND account LIKE CONCAT('%', #{account}, '%') " +
            "</if> " +
            "<if test='employmentDate != null'> " +
            "   AND employment_date >= #{employmentDate} " +
            "</if>" +
            "</script>")
    long countUsersByConditions(
            @Param("userName") String userName,
            @Param("account") String account,
            @Param("employmentDate") String employmentDate);
    
    
    /*插入用户（使用自增ID）*/
    @Insert("INSERT INTO t_user(account, user_name, password, type, tel, email, employment_date,state) " +
            "VALUES(#{account}, #{userName}, #{password}, #{type}, #{tel}, #{email}, #{employmentDate},#{state})")
    @Options(useGeneratedKeys = true, keyProperty = "userId", keyColumn = "user_id")
    int insertUser(User user);
    /*查账号名是否已存在*/
    @Select("SELECT COUNT(*) FROM t_user WHERE account = #{account}")
    boolean existsByAccount(@Param("account") String account);
    
    /*根据ID查询用户*/
    @Select("SELECT * FROM t_user WHERE user_id = #{userId} ")
    User findById(@Param("userId") Integer userId);
    
    /*更新用户信息*/
    @Update("UPDATE t_user SET " +
            "user_name = #{userName}, " +
            "account = #{account}, " +
            "type = #{type}, " +
            "state = #{state}, " +
            "password = #{password}, " +
            "tel = #{tel}, " +
            "email = #{email}, " +
            "employment_date = #{employmentDate} " +
            "WHERE user_id = #{userId}")
    int updateUser(User user);
    
    /*逻辑删除用户（更新state为0）*/
    @Update("UPDATE t_user SET state = 0 WHERE user_id = #{userId}")
    int logicalDelete(@Param("userId") Integer userId);
    /*删除老人的护工*/
    @Update("UPDATE t_check_in_record SET user_id=NULL WHERE user_id = #{userId} and state=1")
    int logicalDeleteUser(@Param("userId") Integer userId);
    
    /*检查用户是否存在且未删除*/
    @Select("SELECT COUNT(*) FROM t_user WHERE user_id = #{userId} AND state = 1")
    boolean existsActiveUser(@Param("userId") Integer userId);
    
    /*统计护工数*/
    @Select("SELECT COUNT(*) FROM t_user WHERE state = 1 AND type=1")
    int countNurse();
    
}
