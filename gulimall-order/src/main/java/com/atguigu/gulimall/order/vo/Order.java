package com.atguigu.gulimall.order.vo;

import lombok.Data;

/**
 * @author Administrator
 * @create 2019-08-19 21:12
 */
@Data
public class Order {
    private Long orderId;
    private Integer status;
    private String desc;
}
