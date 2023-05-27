package com.example.Mapper;

import com.example.Utility.Member;
import com.example.Utility.User1;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

    //通过邮箱和用户名查询用户
    @Select("select * from db_user where username=#{context} or email=#{context}")
    User1 SelectUserByUsernameAndEmail(String context);

    @Select("select * from db_user where username=#{context} or email=#{context}")
    Member SelectUserByUsernameAndEmail1(String context);

    //通过用户名查询用户
    @Select("select * from db_user where username=#{username}")
    User1 SelectUserByUsername(String username);

    //创建用户
    @Insert("insert into db_user values (null,#{email},#{username},#{password})")
    int AddUser(@Param("username") String username,@Param("email") String email,@Param("password") String password);

    //重置密码
    @Update("update db_user set password=#{password} where email=#{email}")
    int UpdatePasswordByEmail(@Param("password") String password,@Param("email") String email);
}


















