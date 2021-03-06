package com.atguigu.gulimall.sms;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author Administrator
 * @create 2019-08-01 23:46
 */
@EnableFeignClients
@RefreshScope
@SpringBootApplication
@EnableSwagger2
@MapperScan(basePackages = "com.atguigu.gulimall.sms.dao")
public class GuliMallSmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(GuliMallSmsApplication.class,args);
    }
}
