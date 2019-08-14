package com.atguigu.gulimall.pms.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @author Administrator
 * @create 2019-08-13 11:15
 */
@Component
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GuliCache {

    String prefix() default "cache";
}
