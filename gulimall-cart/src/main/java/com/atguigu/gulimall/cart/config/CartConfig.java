package com.atguigu.gulimall.cart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Administrator
 * @create 2019-08-17 20:19
 */
@Configuration
public class CartConfig {

    //配置线程池 用于异步调用
    @Bean("mainExecutor")
    @Primary //默认就是这个线程池干活
    public ThreadPoolExecutor mainThreadPoolExecutor(){

        ThreadPoolExecutor executor = new ThreadPoolExecutor(4,1000,0L,
                TimeUnit.SECONDS,new LinkedBlockingQueue<>(Integer.MAX_VALUE/2));

        return executor;

    }

    @Bean("otherExecutor")
    public ThreadPoolExecutor otherThreadPoolExecutor(){

        ThreadPoolExecutor executor = new ThreadPoolExecutor(4,1000,0L,
                TimeUnit.SECONDS,new LinkedBlockingQueue<>(Integer.MAX_VALUE/2));

        return executor;

    }

}
