package com.example.Config;

import com.example.Interceptor.MemberInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
public class WebConfig implements WebMvcConfigurer {
//    //解决前后端分离跨域问题,并没有解决
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")//设置允许跨域的路径
//                .allowedOrigins("http://127.0.0.1:8080")//设置允许跨域请求的域名
//                .allowCredentials(true)//是否允许证书 不再默认开启
//                .allowedMethods("GET", "POST", "PUT", "DELETE")//设置允许的方法
//                .maxAge(3600);//跨域允许时间
//    }

    @Resource
    MemberInterceptor memberInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(memberInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/api/auth/**");
    }

}
