package com.atguigu.gulimall.cart.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Administrator
 * @create 2019-08-16 14:38
 */
@Data
public class SkuCouponVo {

    private Long skuId;
    private Long couponId;//优惠券id
    private String desc;//优惠券描述
    private BigDecimal amount;//优惠券的金额
}
