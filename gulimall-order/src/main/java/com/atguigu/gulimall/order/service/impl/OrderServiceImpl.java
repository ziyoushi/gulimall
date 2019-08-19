package com.atguigu.gulimall.order.service.impl;

import com.atguigu.gulimall.order.service.OrderService;
import com.atguigu.gulimall.order.vo.Order;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Administrator
 * @create 2019-08-19 21:14
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    RabbitTemplate rabbitTemplate;

    ScheduledExecutorService executorService = Executors.newScheduledThreadPool(10);

    @Override
    public Order createOrder() {
        Order order = new Order();
        order.setOrderId(IdWorker.getId());
        order.setDesc("商品xxxxds");
        order.setStatus(0);
        //订单创建完成就给MQ发送一条消息
//        rabbitTemplate.convertAndSend("orderCreateExchange","create.order",order);

        //利用定时线程池
        //有啥问题？
        executorService.schedule(()->{
            System.out.println(order+"已经过期，正准备查询数据库，决定是否关单");
        },30, TimeUnit.SECONDS);

        return order;
    }

}
