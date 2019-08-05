package com.atguigu.gulimall.commons.to;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Administrator
 * @create 2019-08-05 15:34
 */
@Data
public class SkuSaleInfoTo {
    private Long skuId;
    private BigDecimal buyBounds;
    private BigDecimal growBounds;
    private Integer[] work;

    private BigDecimal fullCount;
    private BigDecimal discount;
    private Integer ladderAddOther;

    private BigDecimal fullPrice;
    private BigDecimal reducePrice;

    private Integer fullAddOther;
}
