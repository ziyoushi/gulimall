package com.atguigu.gulimall.oms.dao;

import com.atguigu.gulimall.oms.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 * 
 * @author changchen
 * @email doublechang123@163.com
 * @date 2019-08-01 21:18:17
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
}
