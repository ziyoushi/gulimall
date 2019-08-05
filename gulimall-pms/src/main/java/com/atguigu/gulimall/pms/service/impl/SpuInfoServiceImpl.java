package com.atguigu.gulimall.pms.service.impl;

import com.atguigu.gulimall.commons.bean.PageVo;
import com.atguigu.gulimall.commons.bean.Query;
import com.atguigu.gulimall.commons.bean.QueryCondition;
import com.atguigu.gulimall.commons.to.SkuSaleInfoTo;
import com.atguigu.gulimall.commons.utils.AppUtils;
import com.atguigu.gulimall.pms.dao.*;
import com.atguigu.gulimall.pms.entity.*;
import com.atguigu.gulimall.pms.feign.SmsSkuSaleFeignService;
import com.atguigu.gulimall.pms.service.SpuInfoService;
import com.atguigu.gulimall.pms.vo.BaseAttrVo;
import com.atguigu.gulimall.pms.vo.SaleAttrsVo;
import com.atguigu.gulimall.pms.vo.SkusVo;
import com.atguigu.gulimall.pms.vo.SpuAllSaveVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@Slf4j
@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {

    @Autowired
    private SpuInfoDao spuInfoDao;

    @Autowired
    private SpuInfoDescDao spuInfoDescDao;

    @Autowired
    private ProductAttrValueDao spuAttrValueDao;

    @Autowired
    private SkuInfoDao skuInfoDao;

    @Autowired
    private SkuImagesDao skuImagesDao;

    @Autowired
    private AttrDao attrDao;

    @Autowired
    private SkuSaleAttrValueDao skuSaleAttrValueDao;

    @Autowired
    private SmsSkuSaleFeignService feignService;

    @Override
    public PageVo queryPage(QueryCondition params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<SpuInfoEntity>()
        );

        return new PageVo(page);
    }

    @Override
    public PageVo queryPageByCatId(QueryCondition queryCondition, Long catId) {

        //封装查询条件
        QueryWrapper<SpuInfoEntity> queryWrapper = new QueryWrapper<>();
        if (catId != 0){

            queryWrapper.eq("catalog_id",catId);
            if (!StringUtils.isEmpty(queryCondition.getKey())){
                queryWrapper.and(obj ->{
                    obj.like("spu_name",queryCondition.getKey());
                    obj.or().like("id",queryCondition.getKey());
                    return  obj;
                });
            }

        }

        //封装分页数据
        IPage<SpuInfoEntity> page = new Query<SpuInfoEntity>().getPage(queryCondition);

        IPage<SpuInfoEntity> data = this.page(page,queryWrapper);

        PageVo pageVo = new PageVo(data);
        return pageVo;

    }

    @Override
    public void saveAllSpuBase(SpuAllSaveVo spuInfo) {

        //保存基本spu信息
        Long spuId = this.saveSpuBaseInfo(spuInfo);
        //根据spuId保存spu描述信息(图片信息)
        this.saveSpuInfoDesc(spuId,spuInfo);
        //保存spu基本属性
        this.saveSpuBaseAttrs(spuId,spuInfo);

        //保存sku基本信息
        this.saveSkuInfo(spuId,spuInfo.getSkus());


    }

    @Override
    public Long saveSpuBaseInfo(SpuAllSaveVo spuInfo) {

        SpuInfoEntity spuInfoEntity = new SpuInfoEntity();
        BeanUtils.copyProperties(spuInfo,spuInfoEntity);
        spuInfoEntity.setCreateTime(new Date());
        spuInfoEntity.setUodateTime(new Date());
        spuInfoDao.insert(spuInfoEntity);
        return spuInfoEntity.getId();
    }

    @Override
    public void saveSpuInfoDesc(Long spuId, SpuAllSaveVo spuInfo) {

        String[] spuImages = spuInfo.getSpuImages();

        SpuInfoDescEntity spuInfoDescEntity = new SpuInfoDescEntity();
        spuInfoDescEntity.setSpuId(spuId);
        spuInfoDescEntity.setDecript(AppUtils.arrayToString(spuImages,","));
        spuInfoDescDao.saveSpuInfoDesc(spuInfoDescEntity);

    }

    @Override
    public void saveSpuBaseAttrs(Long spuId, SpuAllSaveVo spuInfo) {

        List<ProductAttrValueEntity> spuAttrList = new ArrayList<>();

        List<BaseAttrVo> baseAttrs = spuInfo.getBaseAttrs();
        baseAttrs.forEach(item ->{
            ProductAttrValueEntity productAttrValueEntity = new ProductAttrValueEntity();
            Long attrId = item.getAttrId();
            String attrName = item.getAttrName();
            String[] valueSelected = item.getValueSelected();
            productAttrValueEntity.setAttrId(attrId);
            productAttrValueEntity.setAttrName(attrName);
            productAttrValueEntity.setAttrValue(AppUtils.arrayToString(valueSelected,","));
            productAttrValueEntity.setSpuId(spuId);
            productAttrValueEntity.setAttrSort(0);
            productAttrValueEntity.setQuickShow(1);
            spuAttrList.add(productAttrValueEntity);
        });

        spuAttrValueDao.saveSpuAttrs(spuAttrList);

    }

    @Override
    public void saveSkuInfo(Long spuId, List<SkusVo> skus) {

        //根据spuId查询出brand_id
        SpuInfoEntity spuInfoEntity = this.getById(spuId);
        Long brandId = spuInfoEntity.getBrandId();
        List<SkuSaleInfoTo> skuSaleInfoTos = new ArrayList<>();

        for (int i = 0; i < skus.size(); i++) {
            SkuInfoEntity skuInfoEntity = new SkuInfoEntity();
            skuInfoEntity.setBrandId(brandId);
            skuInfoEntity.setSpuId(spuId);
            skuInfoEntity.setSkuName(skus.get(i).getSkuName());
            skuInfoEntity.setSkuDesc(skus.get(i).getSkuDesc());
            skuInfoEntity.setSkuTitle(skus.get(i).getSkuTitle());
            skuInfoEntity.setWeight(skus.get(i).getWeight());
            skuInfoEntity.setPrice(skus.get(i).getPrice());
            skuInfoEntity.setSkuCode(UUID.randomUUID().toString().substring(0,5));
            skuInfoDao.insert(skuInfoEntity);

            Long skuId = skuInfoEntity.getSkuId();
            String[] images = skus.get(i).getImages();

            //保存sku图片信息
            if (images != null){
                for (String image : images) {
                    SkuImagesEntity skuImagesEntity = new SkuImagesEntity();
                    skuImagesEntity.setSkuId(skuId);
                    skuImagesEntity.setDefaultImg(i==0?1:0);
                    skuImagesEntity.setImgUrl(image);
                    skuImagesEntity.setImgSort(0);
                    skuImagesDao.insert(skuImagesEntity);

                }
            }

            //保存sku 销售属性
            //saleAttrs
            List<SaleAttrsVo> saleAttrs = skus.get(i).getSaleAttrs();
            saleAttrs.forEach(item ->{
                SkuSaleAttrValueEntity entity = new SkuSaleAttrValueEntity();
                Long attrId = item.getAttrId();
                //根据attrId查出attrName
                AttrEntity attrEntity = attrDao.selectById(attrId);
                String attrName = attrEntity.getAttrName();
                entity.setAttrId(attrId);
                entity.setSkuId(skuId);
                entity.setAttrName(attrName);
                entity.setAttrSort(0);
                entity.setAttrValue(item.getAttrValue());
                skuSaleAttrValueDao.insert(entity);

            });

            SkuSaleInfoTo skuSaleInfoTo = new SkuSaleInfoTo();
            BeanUtils.copyProperties(skus.get(i),skuSaleInfoTo);
            skuSaleInfoTo.setSkuId(skuId);
            skuSaleInfoTos.add(skuSaleInfoTo);

        }

        log.debug("pms系统准备调用sms,{}"+skuSaleInfoTos);
        //下面远程调用sms系统
        feignService.saveSkuSaleBounds(skuSaleInfoTos);
        log.debug("调用成功");

    }

}