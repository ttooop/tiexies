package com.example.tiexies;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.cloud.openfeign.EnableFeignClients;
//import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
//@EnableFeignClients({"org.jwcloud.security.auth.feign"})
//@ComponentScan(basePackages = {"org.jwcloud" , "com.example.tiexies"})
//@MapperScan({"org.jwcloud.mybatis.wrapper.mapper","org.jwcloud.security.auth.mapper"})
public class TiexiesApplication {

    public static void main(String[] args) {
        SpringApplication.run(TiexiesApplication.class, args);
    }

}
