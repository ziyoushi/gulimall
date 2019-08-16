package com.atguigu.lock.test;

import com.atguigu.lock.test.bean.User;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Administrator
 * @create 2019-08-15 23:41
 */
public class CacheUtils {

    private static Map<String,User> map = new HashMap<>();

    public static User getFromCache(String username){
        User user = map.get(username);
        return user;
    }

    public static void saveToCache(User user){

        map.put(user.getUsername(),user);
    }

    //清除缓存
    public static void removeCache(String key){
        map.remove(key);
    }




}
