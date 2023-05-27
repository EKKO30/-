package com.example;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
class StudyPjApplicationTests {

    @Test
    void contextLoads() {
        BCryptPasswordEncoder cryptPasswordEncoder=new BCryptPasswordEncoder();
        System.out.println(cryptPasswordEncoder.encode("123456"));
    }

}
