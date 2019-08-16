package com.atguigu.gulimall.pms.service.impl;

import com.atguigu.gulimall.commons.bean.PageVo;
import com.atguigu.gulimall.commons.bean.Query;
import com.atguigu.gulimall.commons.bean.QueryCondition;
import com.atguigu.gulimall.commons.to.SkuInfoVo;
import com.atguigu.gulimall.pms.dao.SkuInfoDao;
import com.atguigu.gulimall.pms.dao.SkuSaleAttrValueDao;
import com.atguigu.gulimall.pms.entity.SkuInfoEntity;
import com.atguigu.gulimall.pms.entity.SkuSaleAttrValueEntity;
import com.atguigu.gulimall.pms.service.SkuInfoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service("skuInfoService")
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoDao, SkuInfoEntity> implements SkuInfoService {

    @Autowired
    SkuInfoDao skuInfoDao;

    @Autowired
    SkuSaleAttrValueDao skuSaleAttrValueDao;

    @Override
    public PageVo queryPage(QueryCondition params) {
        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                new QueryWrapper<SkuInfoEntity>()
        );

        return new PageVo(page);
    }

    @Override
    public SkuInfoVo getSkuInfo(Long skuId) {
        SkuInfoEntity sku = skuInfoDao.selectById(skuId);
        SkuInfoVo vo = new SkuInfoVo();
        vo.setPics(sku.getSkuDefaultImg());
        vo.setPrice(sku.getPrice());
        vo.setSkuId(skuId);
        vo.setSkuTitle(sku.getSkuTitle());

        String meal = "";
        //SELECT * FROM pms_sku_sale_attr_value WHERE sku_id = 4
        List<SkuSaleAttrValueEntity> skuValues = skuSaleAttrValueDao.selectList(new QueryWrapper<SkuSaleAttrValueEntity>().eq("sku_id", skuId));
        for (SkuSaleAttrValueEntity skuValue : skuValues) {
            meal += "-" + skuValue.getAttrValue();;
        }
        //设置套餐
        vo.setSetmeal(meal);

        return vo;
    }

}