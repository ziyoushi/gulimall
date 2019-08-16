package com.atguigu.gulimall.cart.service;

import com.atguigu.gulimall.cart.vo.CartVo;

/**
 * @author Administrator
 * @create 2019-08-16 16:14
 */
public interface CartService {

    //获取购物车信息
    CartVo getCart(String userKey, String authorization);

    //添加购物车
    CartVo addToCart(Long skuId, Integer num, String userKey, String authorization);
}
