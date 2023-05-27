package com.example.Controller;

import com.example.Service.UserService;
import com.example.Utility.Rest;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.*;
import javax.validation.constraints.Pattern;
import java.util.HashMap;

@Validated
@RestController
@RequestMapping("/api/auth")
public class UserController {

    HashMap<Object,String> map=new HashMap<>();

    //邮件地址的正则表达式
    private final static String EMAIL_VERIFY= "^[a-zA-Z0-9_-]+(.[a-zA-Z0-9_-]+)*@[a-zA-Z0-9_-]+(.[a-zA-Z0-9_-]+)+$";

    //用户名的正则表达式
    private final static String USERNAME_VERIFY="^[a-zA-Z一-龥]{2,9}$";

    @Resource
    UserService userService;

    //发送验证码
    @PostMapping("/Register-verifyEmail")
    public Rest verifyEmail(@Pattern(regexp = EMAIL_VERIFY, message = "邮件地址格式不正确") String email, HttpSession session) {
        String s=userService.SendEmailForRegister(email,session.getId());
        if (s == null) {
            return Rest.Success("邮件发送成功，请注意查收");
        } else {
            return Rest.Failure(s);
        }
    }

    @PostMapping("/register")
    public Rest register(@Pattern(regexp = USERNAME_VERIFY,message = "用户名格式不正确,必须由一个或多个大小写字母或中文字符组成") String username,
                         @Length(min=2,max=16,message = "密码格式不正确") String password,
                         @Pattern(regexp = EMAIL_VERIFY, message = "邮件地址格式不正确") String email,
                         @Length(min=6,max=6,message = "验证码格式不正确") String code,
                         HttpSession session){

        String s=userService.ValidDataAndRegister(username,password,email,code,session.getId());
        if(s == null){
            return Rest.Success("注册成功,请登录");
        }else {
            return Rest.Failure(s);
        }
    }

    //给重置密码发送邮件
    @PostMapping("/reset-SendEmail")
    public Rest reset(@Pattern(regexp = EMAIL_VERIFY, message = "邮件地址格式不正确") String email,HttpSession session){
        String s=userService.SendEmailForReset(email,session.getId());
        if (s == null) {
            return Rest.Success("邮件发送成功，请注意查收");
        } else {
            return Rest.Failure(s);
        }
    }

    //验证码重置密码的邮件
    @PostMapping("/reset-verify")
    public Rest resetVerify(@Pattern(regexp = EMAIL_VERIFY, message = "邮件地址格式不正确") String email,
                            HttpSession session, String code, HttpServletRequest request, HttpServletResponse response){

        String s=userService.ValidDataReset(email,code);
        map.put(session.getId(), email);
        if (s == null) {
            session.setAttribute("verify-email",email);
            System.out.println(session.getId());
            return Rest.Success("邮件验证成功");
        } else {
            return Rest.Failure(s);
        }
    }

    @PostMapping("/reset")
    public Rest DoReset(@Length(min=2,max=16,message = "密码格式不正确") String password,
                        HttpServletRequest request){

//        String sessionId = request.getRequestedSessionId();
//        String email=map.get(sessionId);
//        System.out.println(email+"==sdsd");
//        System.out.println(sessionId+"==");

        HttpSession session=request.getSession();
        String email= (String) session.getAttribute("verify-email");
        System.out.println(session.getId());
        if(email==null){
            return Rest.Failure("邮件为空");
        }else {
//            map.remove(sessionId);
            session.removeAttribute("verify-email");
            userService.ResetPassword(email,password);
            return Rest.Success("密码重置成功");
        }
    }
}
