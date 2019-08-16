package com.atguigu.gulimall.cart.feign;

import com.atguigu.gulimall.commons.bean.Resp;
import com.atguigu.gulimall.commons.to.SkuInfoVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author Administrator
 * @create 2019-08-16 18:14
 */
@FeignClient("gulimall-pms")
public interface SkuFeignService {

    @GetMapping("/pms/skuinfo/cart/{skuId}")
    public Resp<SkuInfoVo> getSkuInfoForCart(@PathVariable("skuId") Long skuId);
}
