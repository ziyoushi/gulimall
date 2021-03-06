package com.atguigu.gulimall.cart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@RefreshScope
@EnableDiscoveryClient
@EnableFeignClients
public class GulimallCartApplication {

    public static void main(String[] args) {
        SpringApplication.run(GulimallCartApplication.class, args);
    }

}
