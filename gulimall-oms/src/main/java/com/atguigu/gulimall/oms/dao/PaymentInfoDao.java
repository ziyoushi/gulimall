package com.atguigu.gulimall.oms.dao;

import com.atguigu.gulimall.oms.entity.PaymentInfoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 支付信息表
 * 
 * @author changchen
 * @email doublechang123@163.com
 * @date 2019-08-01 21:18:17
 */
@Mapper
public interface PaymentInfoDao extends BaseMapper<PaymentInfoEntity> {
	
}
