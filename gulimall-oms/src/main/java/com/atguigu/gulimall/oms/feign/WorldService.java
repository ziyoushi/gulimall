package com.atguigu.gulimall.oms.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Administrator
 * @create 2019-08-02 12:26
 */
@Service
@FeignClient(name = "gulimall-pms")
public interface WorldService {

    @GetMapping("/hello")
    public String hello();
}
