package com.atguigu.gulimall.pms.feign;

import com.atguigu.gulimall.commons.bean.Resp;
import com.atguigu.gulimall.commons.to.SkuStockVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author Administrator
 * @create 2019-08-10 16:46
 */
@FeignClient(name = "gulimall-wms")
public interface WmsSkuFeignService {

    @GetMapping("/wms/waresku/skus")
    public Resp<List<SkuStockVo>> skusWareInfos(@RequestBody List<Long> skuIds);
}
