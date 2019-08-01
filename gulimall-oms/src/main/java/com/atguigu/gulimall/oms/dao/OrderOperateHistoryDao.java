package com.atguigu.gulimall.oms.dao;

import com.atguigu.gulimall.oms.entity.OrderOperateHistoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单操作历史记录
 * 
 * @author changchen
 * @email doublechang123@163.com
 * @date 2019-08-01 21:18:17
 */
@Mapper
public interface OrderOperateHistoryDao extends BaseMapper<OrderOperateHistoryEntity> {
	
}
