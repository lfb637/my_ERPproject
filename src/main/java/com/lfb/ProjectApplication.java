package com.lfb;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.lfb.mapper")
public class ProjectApplication {
    public static void main(String[] args){
        SpringApplication.run(ProjectApplication.class,args);
    }
}
