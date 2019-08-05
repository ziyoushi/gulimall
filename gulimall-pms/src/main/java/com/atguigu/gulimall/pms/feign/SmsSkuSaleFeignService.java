package com.atguigu.gulimall.pms.feign;

import com.atguigu.gulimall.commons.bean.Resp;
import com.atguigu.gulimall.commons.to.SkuSaleInfoTo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author Administrator
 * @create 2019-08-05 17:36
 */
//@Service
@FeignClient("gulimall-sms")
public interface SmsSkuSaleFeignService {

    @PostMapping("/sms/skubounds/saleinfo/save")
    public Resp<Object> saveSkuSaleBounds(@RequestBody List<SkuSaleInfoTo> to);
}
