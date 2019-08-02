package com.atguigu.gulimall.pms.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Administrator
 * @create 2019-08-02 12:28
 */
@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello(){
        return "world";
    }
}
