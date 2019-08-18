package com.atguigu.gulimall.sms.service.impl;

import com.atguigu.gulimall.commons.bean.Resp;
import com.atguigu.gulimall.sms.dao.CouponDao;
import com.atguigu.gulimall.sms.dao.SkuFullReductionDao;
import com.atguigu.gulimall.sms.dao.SkuLadderDao;
import com.atguigu.gulimall.sms.entity.CouponEntity;
import com.atguigu.gulimall.sms.entity.SkuFullReductionEntity;
import com.atguigu.gulimall.sms.entity.SkuLadderEntity;
import com.atguigu.gulimall.sms.feign.SkuInfoFeignService;
import com.atguigu.gulimall.sms.service.SkuCouponReductionService;
import com.atguigu.gulimall.sms.to.SkuCouponTo;
import com.atguigu.gulimall.sms.to.SkuInfoTo;
import com.atguigu.gulimall.sms.to.SkuReductionTo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 * @create 2019-08-17 20:55
 */
@Service
public class SkuCouponReductionServiceImpl implements SkuCouponReductionService {

    @Autowired
    private SkuInfoFeignService skuInfoFeignService;

    @Autowired
    private CouponDao couponDao;

    @Autowired
    private SkuLadderDao ladderDao;

    @Autowired
    private SkuFullReductionDao fullReductionDao;

    @Override
    public List<SkuCouponTo> getCoupons(Long skuId) {

        List<SkuCouponTo> couponTos = new ArrayList<>();

        //根据skuId查出spuId
        //根据spuId 查出sms_coupon_spu_relation 找到couponId
        //根据couponId查出SkuCouponTo 封装
        Resp<SkuInfoTo> info = skuInfoFeignService.info(skuId);
        if (info.getData() != null){
            Long spuId = info.getData().getSpuId();
            List<CouponEntity> couponEntities = couponDao.selectCouponsBySpuId(spuId);
            if (couponEntities != null && couponEntities.size() >0){

                for (CouponEntity couponEntity : couponEntities) {
                    SkuCouponTo skuCouponTo = new SkuCouponTo();
                    skuCouponTo.setAmount(couponEntity.getAmount());
                    skuCouponTo.setCouponId(couponEntity.getId());
                    skuCouponTo.setSkuId(skuId);
                    skuCouponTo.setDesc(couponEntity.getCouponName());
                    couponTos.add(skuCouponTo);
                }

            }

        }

        return couponTos;
    }

    @Override
    public List<SkuReductionTo> getRedutions(Long skuId) {

        //0-打折  1-满减
        //满减
        List<SkuFullReductionEntity> fullReduces = fullReductionDao.selectList(new QueryWrapper<SkuFullReductionEntity>().eq("sku_id", skuId));

        //满多件打多少折
        List<SkuLadderEntity> skuLadders = ladderDao.selectList(new QueryWrapper<SkuLadderEntity>().eq("sku_id", skuId));

        List<SkuReductionTo> tos = new ArrayList<>();

        fullReduces.forEach(item ->{

            SkuReductionTo skuReductionTo = new SkuReductionTo();
            BeanUtils.copyProperties(item,skuReductionTo);
            skuReductionTo.setDesc("消费满"+item.getFullPrice()+"，减"+item.getReducePrice()+"");
            skuReductionTo.setType(1);
            tos.add(skuReductionTo);
        });

        skuLadders.forEach(item ->{

            SkuReductionTo skuReductionTo = new SkuReductionTo();
            BeanUtils.copyProperties(item,skuReductionTo);
            skuReductionTo.setDesc("满"+item.getFullCount()+"件，享受"+item.getDiscount()+"折优惠");
            skuReductionTo.setType(0);
            tos.add(skuReductionTo);

        });

        return tos;
    }
}
