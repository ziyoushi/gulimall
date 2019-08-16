package com.atguigu.lock.test.controller;

import com.atguigu.lock.test.CacheUtils;
import com.atguigu.lock.test.bean.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Administrator
 * @create 2019-08-15 23:39
 */
@RestController
public class CacheController {

    @GetMapping("/get")
    public User getUser(@RequestParam("username") String username){
        User cache = CacheUtils.getFromCache(username);

        if (cache == null){
            //如果缓存没有模拟从数据库中查询出来
            return new User("dbuser","dbuser@qq.com");
        }
        return cache;
    }

    //save
    @PostMapping("/save")
    public String saveUser(User user){

        //失效模式  先清缓存 再给数据库保存
        //CacheUtils.removeCache(user.getUsername());
        //模拟给数据库写数据

        //双写模式
        //先给数据库写 成功后 再给缓存写
        System.out.println("给数据库保存");
        CacheUtils.saveToCache(user);

        return "ok";
    }

}
