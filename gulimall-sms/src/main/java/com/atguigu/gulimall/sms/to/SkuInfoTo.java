package com.atguigu.gulimall.sms.to;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Administrator
 * @create 2019-08-17 20:54
 */
@Data
public class SkuInfoTo {
    private Long skuId;

    private Long spuId;

    private String skuCode;
    private String skuName;

    private String skuDesc;

    private Long catalogId;

    private Long brandId;

    private String skuDefaultImg;

    private String skuTitle;

    private String skuSubtitle;

    private BigDecimal price;

    private BigDecimal weight;
}
