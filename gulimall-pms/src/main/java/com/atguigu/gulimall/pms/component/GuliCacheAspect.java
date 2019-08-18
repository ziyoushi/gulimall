package com.atguigu.gulimall.pms.component;

import com.alibaba.fastjson.JSON;
import com.atguigu.gulimall.pms.annotation.GuliCache;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Administrator
 * @create 2019-08-16 22:25
 */
@Slf4j
@Component
@Aspect
public class GuliCacheAspect {

    @Autowired
    private StringRedisTemplate redisTemplate;

    ReentrantLock lock = new ReentrantLock();

    @Around("@annotation(com.atguigu.gulimall.pms.annotation.GuliCache)")
    public Object around(ProceedingJoinPoint point) throws Throwable {

        //为了防止缓存被击穿 加锁

        Object result = null;
        try {

            //获取目标方法的所有参数的值
            Object[] args = point.getArgs();
            //拿到注解的值
            MethodSignature signature = (MethodSignature) point.getSignature();
            GuliCache guliCache = signature.getMethod().getAnnotation(GuliCache.class);

            if (guliCache == null) {
                //说明没有缓存 需要执行操作去数据库查询
                result = point.proceed();
            }

            String prefix = guliCache.prefix();
            if (args != null) {
                for (Object arg : args) {
                    prefix += ":" + arg.toString();
                }
            }

            Object cache = getFromCache(prefix, signature);
            if (cache != null) {
                return cache;
            } else {
                lock.lock();
                log.debug("缓存切面进入工作 返回通知");

                //双检查
                cache = getFromCache(prefix, signature);
                if (cache == null) {
                    //缓存没击中
                    //执行目标方法
                    result = point.proceed();
                    //给redis中设值
                    redisTemplate.opsForValue().set(prefix, JSON.toJSONString(result));
                    //注意一定要返回出去
                    return result;
                } else {
                    return cache;
                }

            }

        } catch (Exception e) {
            log.error("缓存切面介入  异常通知");

        } finally {
            log.debug("缓存切面接入 后置通知");
            //注意如果锁上了 就给他unlock
            if (lock.isLocked()) {

                lock.unlock();
            }

        }
        return result;

    }

    private Object getFromCache(String prefix, MethodSignature signature) {

        String s = redisTemplate.opsForValue().get(prefix);
        //如果s不为空 说明缓存中有数据 目标方法不执行
        if (!StringUtils.isEmpty(s)) {
            Class returnType = ((MethodSignature) signature).getReturnType();
            return JSON.parseObject(s, returnType);
        }
        return null;
    }

    //清缓存 删除缓存
    private void cleanCurrentCache(String prefix){
        redisTemplate.delete(prefix);
    }


}
