package com.example.Config;

import com.alibaba.fastjson.JSONObject;
import com.example.Service.UserService;
import com.example.Utility.Rest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;


//控制与登录有关的操作
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Resource
    UserService userService;

    @Resource
    DataSource dataSource;

    //定义一个Bean来管理登录权限和登录方式
    @Bean//链式操作
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests()//首先需要配置哪些请求会被拦截，哪些请求必须具有什么角色才能访问
                .antMatchers("/api/auth/**").permitAll()
                .anyRequest().authenticated()//任何请求都要检查授权
                .and()
                .formLogin()//配置Form表单登陆
                .loginProcessingUrl("/api/auth/login")//配置登录界面的url
                .successHandler(this::onAuthenticationSuccess)//调用成功的方法
                .failureHandler(this::onAuthenticationFailure)//调用失败的方法
                .and()
                .logout()
                .logoutUrl("/api/auth/logout")//配置登出界面的url
                .logoutSuccessHandler(this::onAuthenticationSuccess)//调用成功的方法
                .and()
                .rememberMe()
                .rememberMeParameter("remember")
                .tokenRepository(this.tokenRepository())
                .tokenValiditySeconds(3600 * 24 * 7)
                .and()
                .userDetailsService(userService)
                .csrf().disable()//关闭csrf防御，防止出现bug
                //解决跨域问题
                .cors()
                .configurationSource(this.configuration())
                .and()
                // 是一种错误处理机制，通常用于优化网站的交互体验
                .exceptionHandling()
                .authenticationEntryPoint(this::onAuthenticationFailure)//出现其他错误
                .and()
                .build();//构建完成
    }


    //使用记住我，存储cookie
    @Bean
    public PersistentTokenRepository tokenRepository(){
        JdbcTokenRepositoryImpl jdbcTokenRepository=new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        //允许创建一张表来存储cookie,使用一次后就可以关闭了
        jdbcTokenRepository.setCreateTableOnStartup(false);
        return jdbcTokenRepository;
    }

    //解开限制,解决跨域问题
    private CorsConfigurationSource configuration(){
        CorsConfiguration configuration=new CorsConfiguration();
        //允许所有的请求,只能用于测试,非常危险
        configuration.addAllowedOriginPattern("*");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("POST");
        configuration.addAllowedMethod("GET");
        //允许携带cookie
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source=new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",configuration);
        return source;
    }

    //登录资源选择
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity security) throws Exception {
        return security
                .getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userService)
                .and()
                .build();
    }

    //密码解析
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    //请求失败返回
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException authenticationException) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().write(JSONObject.toJSONString(Rest.Failure(authenticationException.getMessage())));
    }

    //请求成功返回
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException{
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        if(request.getRequestURI().endsWith("/login"))
            response.getWriter().write(JSONObject.toJSONString(Rest.Success("登录成功")));
        else if(request.getRequestURI().endsWith("/logout"))
            response.getWriter().write(JSONObject.toJSONString(Rest.Success("退出登录成功")));
    }
}




















