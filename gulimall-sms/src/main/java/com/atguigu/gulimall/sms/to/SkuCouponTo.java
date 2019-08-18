package com.atguigu.gulimall.sms.to;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Administrator
 * @create 2019-08-17 20:52
 */
@Data
public class SkuCouponTo {

    private Long skuId; //商品id

    private Long couponId; //优惠券id

    private String desc;//优惠券描述

    private BigDecimal amount;//优惠卷的金额
}
