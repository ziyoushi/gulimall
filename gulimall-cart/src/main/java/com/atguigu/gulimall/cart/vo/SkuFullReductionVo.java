package com.atguigu.gulimall.cart.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Administrator
 * @create 2019-08-16 14:34
 * 满减
 */
@Data
public class SkuFullReductionVo {

    private Long id;
    private Long skuId;
    private String desc;
    //满多少元
    private BigDecimal fullPrice;
    //减多少元
    private BigDecimal reducePrice;
    private Integer addOther;

    //满多少件
    private Integer fullCount;
    //打多少折
    private Integer discount;

    private Integer type;//满减类型 0 打折 1 满减

}
