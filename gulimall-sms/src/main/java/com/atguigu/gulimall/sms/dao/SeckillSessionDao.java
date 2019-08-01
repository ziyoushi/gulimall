package com.atguigu.gulimall.sms.dao;

import com.atguigu.gulimall.sms.entity.SeckillSessionEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 秒杀活动场次
 * 
 * @author changchen
 * @email doublechang123@163.com
 * @date 2019-08-01 21:22:26
 */
@Mapper
public interface SeckillSessionDao extends BaseMapper<SeckillSessionEntity> {
	
}
