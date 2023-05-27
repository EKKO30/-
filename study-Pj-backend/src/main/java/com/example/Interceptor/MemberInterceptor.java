package com.example.Interceptor;

import com.example.Mapper.UserMapper;
import com.example.Utility.Member;
import com.example.Utility.Rest;
import com.example.Utility.User1;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class MemberInterceptor implements HandlerInterceptor {

    @Resource
    UserMapper userMapper;

    //最开始调用的拦截器
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        SecurityContext securityContext= SecurityContextHolder.getContext();
        Authentication authentication=securityContext.getAuthentication();
        System.out.println(authentication.getPrincipal());
        User user= (User) authentication.getPrincipal();
        Member member=userMapper.SelectUserByUsernameAndEmail1(user.getUsername());
        request.getSession().setAttribute("Member",member);
        return true;
    }
}

















