package com.atguigu.gulimall.pms.controller.api;

import com.atguigu.gulimall.commons.bean.Resp;
import com.atguigu.gulimall.pms.service.ItemService;
import com.atguigu.gulimall.pms.vo.SkuItemDetailVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

/**
 * @author Administrator
 * @create 2019-08-18 16:42
 */
@RestController
@RequestMapping
public class ItemController {


    @Autowired
    private ItemService itemService;

    @ApiOperation("获取商品详情")
    @GetMapping("/item/{skuId}.html")
    public Resp<SkuItemDetailVo> skuItemDetails(@PathVariable("skuId") Long skuId) throws ExecutionException, InterruptedException {

        SkuItemDetailVo detailVo = itemService.getDetails(skuId);
        return Resp.ok(detailVo);
    }


}
