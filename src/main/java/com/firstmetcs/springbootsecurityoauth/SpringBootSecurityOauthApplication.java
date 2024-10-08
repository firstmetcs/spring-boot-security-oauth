package com.firstmetcs.springbootsecurityoauth;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.firstmetcs.springbootsecurityoauth.dao")
public class SpringBootSecurityOauthApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootSecurityOauthApplication.class, args);
    }

}
