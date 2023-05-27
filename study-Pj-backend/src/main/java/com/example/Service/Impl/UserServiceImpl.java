package com.example.Service.Impl;

import com.example.Mapper.UserMapper;
import com.example.Service.UserService;
import com.example.Utility.User1;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Value("${spring.mail.username}")
    String from;

    @Resource
    UserMapper userMapper;

    @Resource
    MailSender sender;

    @Resource
    StringRedisTemplate template;

    //检验用户是否存在
    @Override
    public UserDetails loadUserByUsername(String context) throws UsernameNotFoundException {
        if(context==null){
            throw new UsernameNotFoundException("用户名或者密码不能为空");
        }

        User1 user=userMapper.SelectUserByUsernameAndEmail(context);
        if(user==null){
            throw new UsernameNotFoundException("用户名或者密码错误");
        }

        return User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles("user")
                .build();
    }

    //发送验证码
    @Override
    public String SendEmailForRegister(String email,String sessionId) {
//        String EmailKey="email:"+sessionId+":"+email;
        String EmailKey="email:"+email+"Register";
        if(Boolean.TRUE.equals(template.hasKey(EmailKey))){
            //默认为0s
            Long expire = Optional.ofNullable(template.getExpire(EmailKey, TimeUnit.SECONDS)).orElse(0L);
            //剩余时间大于120s则意味着发送验证码时间小于60s，所以不让发送,cd60s
            if(expire>120){
                return "验证码发送频繁";
            }
        }

        if(userMapper.SelectUserByUsernameAndEmail(email)!=null){
            return "邮箱已被占用";
        }

        Random random=new Random();
        int code= random.nextInt(899999)+100000;
        SimpleMailMessage message=new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(email);
        message.setSubject("您的注册验证码");
        message.setText("验证码:"+code);

        try {
            sender.send(message);
            template.opsForValue().set(EmailKey,String.valueOf(code),3, TimeUnit.MINUTES);
            return null;
        }catch (MailException e){
            e.printStackTrace();
            return "请检查邮箱是否填写正确";
        }
    }

    //检查数据和注册
    @Override
    public String ValidDataAndRegister(String username, String password, String email, String code,String sessionId) {
        if(userMapper.SelectUserByUsername(username)!=null){
            return "用户名已存在";
        }

//        String EmailKey="email: "+sessionId +": "+email;
        String EmailKey="email:"+email+"Register";
        if(Boolean.TRUE.equals(template.hasKey(EmailKey))){
            String a=template.opsForValue().get(EmailKey);
            if(a == null){
                return "验证码已失效";
            }

            if(code.equals(a)){
                //加密密码
                password=encoder.encode(password);
                //创建用户
                if(userMapper.AddUser(username,email,password) >0 ){
                    template.delete(EmailKey);
                    return null;
                }else {
                    template.delete(EmailKey);
                    return "创建失败,请重试";
                }
            }else {
                return "验证码错误";
            }
        }else {
            return "请先请求有效验证码";
        }
    }


    @Override
    public String SendEmailForReset(String email,String sessionId){
        //        String EmailKey="email:"+sessionId+":"+email;
        String EmailKey="email:"+email+"Reset";
        if(Boolean.TRUE.equals(template.hasKey(EmailKey))){
            //默认为0s
            Long expire = Optional.ofNullable(template.getExpire(EmailKey, TimeUnit.SECONDS)).orElse(0L);
            //剩余时间大于120s则意味着发送验证码时间小于60s，所以不让发送,cd60s
            if(expire>120){
                return "验证码发送频繁";
            }
        }

        if(userMapper.SelectUserByUsernameAndEmail(email)==null){
            return "此邮箱没有注册无法重置密码";
        }

        Random random=new Random();
        int code= random.nextInt(899999)+100000;
        SimpleMailMessage message=new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(email);
        message.setSubject("您的重置验证码");
        message.setText("验证码"+code);

        try {
            sender.send(message);
            template.opsForValue().set(EmailKey,String.valueOf(code),3, TimeUnit.MINUTES);
            return null;
        }catch (MailException e){
            e.printStackTrace();
            return "请检查邮箱是否填写正确";
        }
    }

    @Override
    public String ValidDataReset(String email, String code) {
        String EmailKey="email:"+email+"Reset";
        if(Boolean.TRUE.equals(template.hasKey(EmailKey))){
            String a=template.opsForValue().get(EmailKey);
            if(a == null){
                return "验证码已失效";
            }

            if(code.equals(a)) {
                return null;
            } else {
                return "验证码错误";
            }
        }else {
            return "请先请求有效验证码";
        }
    }

    @Override
    public Boolean ResetPassword(String password, String email) {
        password=encoder.encode(password);
        int i=userMapper.UpdatePasswordByEmail(password,email);
        return i>0;
    }
}


















