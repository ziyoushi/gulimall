package com.atguigu.gulimall.pms.service;

import com.atguigu.gulimall.commons.to.SkuInfoVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.gulimall.pms.entity.SkuInfoEntity;
import com.atguigu.gulimall.commons.bean.PageVo;
import com.atguigu.gulimall.commons.bean.QueryCondition;


/**
 * sku信息
 *
 * @author changchen
 * @email doublechang123@163.com
 * @date 2019-08-01 23:59:57
 */
public interface SkuInfoService extends IService<SkuInfoEntity> {

    PageVo queryPage(QueryCondition params);

    //根据skuId获取sku供购物车使用
    SkuInfoVo getSkuInfo(Long skuId);
}

