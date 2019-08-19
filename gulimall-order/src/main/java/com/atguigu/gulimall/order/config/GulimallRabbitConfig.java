package com.atguigu.gulimall.order.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Administrator
 * @create 2019-08-19 15:12
 */
@EnableRabbit
@Configuration
public class GulimallRabbitConfig {

    /**
     * 容器中放入自定义的messageConverter消息发送与接收就会用它进行转换
     * @return
     */
    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    /**
     * SpringBoot会自动给RabbitMQ中创建这个交换机/队列/绑定关系
     *
     * 1）、去RabbitMQ里面看有没有当前名字的交换机/队列/绑定关系，如果没有就创建，有就不管了。
     * @return
     */
    @Bean("myExchange-hhhh")
    public Exchange myExchange(){
        /**
         * String name,
         * boolean durable, boolean autoDelete, Map<String, Object> arguments
         */
        FanoutExchange fanoutExchange = new FanoutExchange("myExchange-hhhh",true,false,null);
        System.out.println("即将自动创建一个myExchange-hhhh");
        return fanoutExchange;
    }

    @Bean("myqueue-hahha")
    public Queue myqueue(){
        return new Queue("myqueue-hahha",true,false,false,null);
    }

    @Bean("mybinding-haha")
    public Binding mybinding(){

        return new Binding("myqueue-hahha",
                Binding.DestinationType.QUEUE,
                "myExchange-hhhh","hello",null);
    }

}
