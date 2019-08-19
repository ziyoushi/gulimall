package com.atguigu.gulimall.order.controller;

import com.atguigu.gulimall.order.service.OrderService;
import com.atguigu.gulimall.order.vo.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Administrator
 * @create 2019-08-19 21:22
 */
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    OrderService orderService;
    /**
     * 创建订单
     * @return
     */
    @GetMapping("/create")
    public Order createOrder(){

        Order order =  orderService.createOrder();

        return order;
    }
}
