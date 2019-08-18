package com.atguigu.gulimall.commons.bean;

/**
 * @author Administrator
 * @create 2019-08-10 18:42
 */
public class Constant {
    public static  final String ES_SPU_INDEX = "gulimall";
    public static  final String ES_SPU_TYPE = "spu";

    public static final String CACHE_CATELOG = "cache:catelog:";

    public static final String LOGIN_USER_PREFIX = "login:user:";

    //登录超时
    public static final Long LOGIN_USER_TIMEOUT_MINUTES = 30L;

    //购物车前缀
    public static final String CART_PREFIX = "cart:user:";

    //设置购物车超期时间
    public static final Long CART_TIMEOUT = 60 * 24 * 30L;

}
