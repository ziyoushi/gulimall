package com.atguigu.gulimall.pms.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Administrator
 * @create 2019-08-02 11:40
 */
@Configuration
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.atguigu.gulimall.pms.feign")
public class PmsCloudConfig {

    //配置线程池
    @Bean("mainThreadPool")
    public ThreadPoolExecutor mainPool(@Value("${spring.mainThread.corePoolSize}") Integer corePoolSize,
                                       @Value("${spring.mainThread.maximumPoolSize}") Integer maximumPoolSize){

        /**
         * int corePoolSize,
         *  int maximumPoolSize,
         *  long keepAliveTime,
         *  TimeUnit unit,
         *  BlockingQueue<Runnable> workQueue
         *
         */
        return new ThreadPoolExecutor(
                corePoolSize,
                maximumPoolSize,
                0L,
                TimeUnit.SECONDS, new LinkedBlockingQueue<>(Integer.MAX_VALUE/2));
    }

}
