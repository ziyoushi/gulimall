package com.atguigu.gulimall.pms;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author Administrator
 * @create 2019-08-01 23:49
 */

@SpringBootApplication
@EnableSwagger2
@MapperScan("com.atguigu.gulimall.pms.dao")
public class GuliMallPmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(GuliMallPmsApplication.class,args);
    }
}
