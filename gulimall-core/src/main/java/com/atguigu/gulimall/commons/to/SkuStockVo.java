package com.atguigu.gulimall.commons.to;

import lombok.Data;

/**
 * @author Administrator
 * @create 2019-08-10 15:38
 */
@Data
public class SkuStockVo {
    private Long skuId;
    //库存数
    private Integer stock;
}
