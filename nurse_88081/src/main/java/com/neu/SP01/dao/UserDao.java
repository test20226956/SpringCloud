package com.neu.SP01.dao;

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
    User findUserByAccount(@Param("account") String account);
    
}
