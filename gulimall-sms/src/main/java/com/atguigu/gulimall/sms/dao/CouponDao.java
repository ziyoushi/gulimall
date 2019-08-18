package com.atguigu.gulimall.sms.dao;

import com.atguigu.gulimall.sms.entity.CouponEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 优惠券信息
 * 
 * @author changchen
 * @email doublechang123@163.com
 * @date 2019-08-02 00:03:39
 */
@Mapper
public interface CouponDao extends BaseMapper<CouponEntity> {
    List<CouponEntity> selectCouponsBySpuId(Long spuId);
}
