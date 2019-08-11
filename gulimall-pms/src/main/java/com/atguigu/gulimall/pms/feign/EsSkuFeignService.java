package com.atguigu.gulimall.pms.feign;

import com.atguigu.gulimall.commons.bean.Resp;
import com.atguigu.gulimall.commons.to.es.EsSkuVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author Administrator
 * @create 2019-08-10 18:39
 */
@FeignClient(name = "gulimall-search")
public interface EsSkuFeignService {

    @PostMapping("/es/spu/up")
    public Resp<Object> spuUp(@RequestBody List<EsSkuVo> vos);

}
