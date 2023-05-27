package com.example.Service;

import org.springframework.security.core.userdetails.UserDetailsService;


public interface UserService extends UserDetailsService {
    //给注册发送邮件
    String SendEmailForRegister(String email,String sessionId);

    //验证数据和注册
    String ValidDataAndRegister(String username, String password, String email, String code, String sessionId);

    //给重置密码发送邮件
    String SendEmailForReset(String email,String sessionId);

    //重置密码的相关验证
    String ValidDataReset(String email,String code);

    //重置密码
    Boolean ResetPassword(String password,String email);
}
