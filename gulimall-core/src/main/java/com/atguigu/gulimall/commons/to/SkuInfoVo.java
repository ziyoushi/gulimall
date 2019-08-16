package com.atguigu.gulimall.commons.to;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Administrator
 * @create 2019-08-16 18:19
 */
@Data
public class SkuInfoVo {

    private Long skuId;
    private String skuTitle;//sku商品标题
    private String setmeal;//套餐
    private String pics;//图片
    private BigDecimal price;//商品价格
}
