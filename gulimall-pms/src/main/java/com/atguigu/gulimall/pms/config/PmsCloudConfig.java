package com.atguigu.gulimall.pms.config;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

/**
 * @author Administrator
 * @create 2019-08-02 11:40
 */
@Configuration
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.atguigu.gulimall.pms.feign")
public class PmsCloudConfig {
}
