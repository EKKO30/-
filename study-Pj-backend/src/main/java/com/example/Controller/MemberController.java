package com.example.Controller;

import com.example.Utility.Member;
import com.example.Utility.Rest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

@RestController
@RequestMapping("/api/member")
public class MemberController {

    @GetMapping("/me")
    public Rest ShowMe(@SessionAttribute("Member")Member member){
        return Rest.Success(member);
    }
}
