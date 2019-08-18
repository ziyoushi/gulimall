package com.atguigu.gulimall.sms.service;

import com.atguigu.gulimall.sms.to.SkuCouponTo;
import com.atguigu.gulimall.sms.to.SkuReductionTo;

import java.util.List;

/**
 * @author Administrator
 * @create 2019-08-17 20:50
 */
public interface SkuCouponReductionService {
    //获取优惠券
    List<SkuCouponTo> getCoupons(Long skuId);

    //获取满减
    List<SkuReductionTo> getRedutions(Long skuId);
}
