package com.atguigu.gulimall.sms;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author Administrator
 * @create 2019-08-01 23:46
 */
@SpringBootApplication
@EnableSwagger2
@MapperScan("com.atguigu.gulimall.sms.dao")
public class GuliMallSmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(GuliMallSmsApplication.class,args);
    }
}
