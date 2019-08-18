package com.atguigu.gulimall.sms.feign;

import com.atguigu.gulimall.commons.bean.Resp;
import com.atguigu.gulimall.sms.to.SkuInfoTo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author Administrator
 * @create 2019-08-17 21:01
 */
@FeignClient("gulimall-pms")
public interface SkuInfoFeignService {

    @GetMapping("/pms/skuinfo/info/{skuId}")
    public Resp<SkuInfoTo> info(@PathVariable("skuId") Long skuId);
}
