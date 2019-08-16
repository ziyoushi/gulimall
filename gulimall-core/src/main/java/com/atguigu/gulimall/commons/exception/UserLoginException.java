package com.atguigu.gulimall.commons.exception;

/**
 * @author Administrator
 * @create 2019-08-16 10:18
 * 登录异常
 */
public class UserLoginException extends RuntimeException {

    public UserLoginException(String msg){
        super(msg);
    }
}
