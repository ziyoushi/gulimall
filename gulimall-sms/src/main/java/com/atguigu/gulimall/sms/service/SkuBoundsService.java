package com.atguigu.gulimall.sms.service;

import com.atguigu.gulimall.commons.to.SkuSaleInfoTo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.gulimall.sms.entity.SkuBoundsEntity;
import com.atguigu.gulimall.commons.bean.PageVo;
import com.atguigu.gulimall.commons.bean.QueryCondition;

import java.util.List;


/**
 * 商品sku积分设置
 *
 * @author changchen
 * @email doublechang123@163.com
 * @date 2019-08-02 00:03:39
 */
public interface SkuBoundsService extends IService<SkuBoundsEntity> {

    PageVo queryPage(QueryCondition params);

    //保存sku营销信息
    void saveSkuAllSaleInfo(List<SkuSaleInfoTo> to);
}

