package com.atguigu.gulimall.oms.controller;

import com.atguigu.gulimall.oms.feign.WorldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Administrator
 * @create 2019-08-02 12:31
 */
@RestController
public class WorldController {

    @Autowired
    private WorldService worldService;

    @GetMapping("/getWorld")
    public String get(){
        String msg = "";
        msg = worldService.hello();

        return "hello " + msg;
    }
}
