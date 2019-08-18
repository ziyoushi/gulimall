package com.atguigu.gulimall.pms.component;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * @author Administrator
 * @create 2019-08-16 22:25
 */
@Component
@Aspect
public class GuliCacheAspect {


    @Around("@annotation(com.atguigu.gulimall.pms.annotation.GuliCache)")
    public Object around(ProceedingJoinPoint point) throws Throwable {

        Object proceed = point.proceed();

//        AnnotatedElementUtils.findAllMergedAnnotations()

        return proceed;
    }
}
