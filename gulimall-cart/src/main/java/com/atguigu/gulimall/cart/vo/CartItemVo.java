package com.atguigu.gulimall.cart.vo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Administrator
 * @create 2019-08-16 14:30
 */

public class CartItemVo {

    @Setter
    @Getter
    private Long skuId;
    @Setter @Getter
    private String skuTitle;
    @Setter @Getter
    private String setmeal;//套餐

    @Setter @Getter
    private String pics;//图片

    @Setter @Getter
    private BigDecimal price;
    @Setter @Getter
    private Integer num;

    @Setter @Getter
    private boolean check = true;//商品是否选中 默认选中 对应status ==0 false ==1 true

    private BigDecimal totalPrice;

    @Setter @Getter
    private List<SkuFullReductionVo> reductionVos;

    @Setter @Getter
    private List<SkuCouponVo> couponVos;

    //todo 计算总价格
    public BigDecimal getTotalPrice() {

        return price.multiply(new BigDecimal(num+""));
    }
}
