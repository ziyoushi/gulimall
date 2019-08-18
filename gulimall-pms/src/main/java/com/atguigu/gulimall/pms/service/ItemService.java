package com.atguigu.gulimall.pms.service;

import com.atguigu.gulimall.pms.vo.SkuItemDetailVo;

import java.util.concurrent.ExecutionException;

/**
 * @author Administrator
 * @create 2019-08-18 19:42
 */
public interface ItemService {
    //获取商品详情
    SkuItemDetailVo getDetails(Long skuId) throws ExecutionException, InterruptedException;
}
