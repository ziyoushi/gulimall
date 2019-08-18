package com.atguigu.gulimall.cart.service;

import com.atguigu.gulimall.cart.vo.CartVo;

import java.util.concurrent.ExecutionException;

/**
 * @author Administrator
 * @create 2019-08-16 16:14
 */
public interface CartService {

    //获取购物车信息
    CartVo getCart(String userKey, String authorization) throws ExecutionException, InterruptedException;

    //添加购物车
    CartVo addToCart(Long skuId, Integer num, String userKey, String authorization) throws ExecutionException, InterruptedException;

    //修改购物车商品数量
    CartVo updateCart(Long skuId, Integer num, String userKey, String authorization);

    //是否选中
    CartVo checkCart(Long[] skuIds, Integer status, String userKey, String authorization);
}
