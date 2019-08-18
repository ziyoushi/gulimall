package com.atguigu.gulimall.cart.feign;

import com.atguigu.gulimall.cart.vo.SkuCouponVo;
import com.atguigu.gulimall.cart.vo.SkuFullReductionVo;
import com.atguigu.gulimall.commons.bean.Resp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @author Administrator
 * @create 2019-08-17 21:41
 */
@FeignClient("gulimall-sms")
public interface SkuCouponReductionFeignService {

    @GetMapping("/sms/sku/coupon/{skuId}")
    public Resp<List<SkuCouponVo>> getCoupons(@PathVariable("skuId") Long skuId);

    @GetMapping("/sms/sku/reduction/{skuId}")
    public Resp<List<SkuFullReductionVo>> getRedutions(@PathVariable("skuId") Long skuId);
}
