package com.atguigu.gulimall.pms.service.impl;

import com.atguigu.gulimall.commons.bean.PageVo;
import com.atguigu.gulimall.commons.bean.Query;
import com.atguigu.gulimall.commons.bean.QueryCondition;
import com.atguigu.gulimall.commons.bean.Resp;
import com.atguigu.gulimall.commons.to.SkuSaleInfoTo;
import com.atguigu.gulimall.commons.to.SkuStockVo;
import com.atguigu.gulimall.commons.to.es.EsSkuAttributeValue;
import com.atguigu.gulimall.commons.to.es.EsSkuVo;
import com.atguigu.gulimall.commons.utils.AppUtils;
import com.atguigu.gulimall.pms.dao.*;
import com.atguigu.gulimall.pms.entity.*;
import com.atguigu.gulimall.pms.feign.EsSkuFeignService;
import com.atguigu.gulimall.pms.feign.SmsSkuSaleFeignService;
import com.atguigu.gulimall.pms.feign.WmsSkuFeignService;
import com.atguigu.gulimall.pms.service.SpuInfoService;
import com.atguigu.gulimall.pms.vo.BaseAttrVo;
import com.atguigu.gulimall.pms.vo.SaleAttrsVo;
import com.atguigu.gulimall.pms.vo.SkusVo;
import com.atguigu.gulimall.pms.vo.SpuAllSaveVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Autowired
    private BrandDao brandDao;

    @Autowired
    private CategoryDao categoryDao;
    
    @Autowired
    private WmsSkuFeignService wmsSkuFeignService;

    @Autowired
    private EsSkuFeignService esSkuFeignService;

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

    @GlobalTransactional(rollbackFor = Exception.class)
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

    @Transactional
    @Override
    public Long saveSpuBaseInfo(SpuAllSaveVo spuInfo) {

        SpuInfoEntity spuInfoEntity = new SpuInfoEntity();
        BeanUtils.copyProperties(spuInfo,spuInfoEntity);
        spuInfoEntity.setCreateTime(new Date());
        spuInfoEntity.setUodateTime(new Date());
        spuInfoDao.insert(spuInfoEntity);
        return spuInfoEntity.getId();
    }

    @Transactional
    @Override
    public void saveSpuInfoDesc(Long spuId, SpuAllSaveVo spuInfo) {

        String[] spuImages = spuInfo.getSpuImages();

        SpuInfoDescEntity spuInfoDescEntity = new SpuInfoDescEntity();
        spuInfoDescEntity.setSpuId(spuId);
        spuInfoDescEntity.setDecript(AppUtils.arrayToString(spuImages,","));
        spuInfoDescDao.saveSpuInfoDesc(spuInfoDescEntity);

    }

    @Transactional
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

    @Transactional
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

    @Override
    public void updateSpuStatus(Long spuId, Integer status) {

        if (status == 1){
            //上架
            spuUp(spuId,status);
        }else {
            //下架
            spuDown(spuId,status);
        }
    }

    //下架
    private void spuDown(Long spuId, Integer status) {

        //下架 将商品从es中删除

    }

    //上架
    private void spuUp(Long spuId, Integer status) {
        //根据spuId查出商品的基本信息 封装数据 不需要每个sku都去查询一遍
        SpuInfoEntity spuInfoEntity = spuInfoDao.selectById(spuId);
        //根据brandId查出品牌名
        BrandEntity brandEntity = brandDao.selectById(spuInfoEntity.getBrandId());
        //查出出分类名
        CategoryEntity categoryEntity = categoryDao.selectById(spuInfoEntity.getCatalogId());

        //上架 -》将商品需要检索的信息存放到es中
        List<EsSkuVo> esSkuVoList = new ArrayList<>();

        //查询出所有spuId对应的sku商品
        List<SkuInfoEntity> skus = skuInfoDao.selectList(new QueryWrapper<SkuInfoEntity>().eq("spu_id", spuId));

        //根据skuId查询出每个sku商品对应的库存 需要远程调用库存系统
        //将skuId封装 避免多次调用 网络问题引起 查询失败
        List<Long> skuIds = new ArrayList<>();
        skus.forEach(sku ->{
            skuIds.add(sku.getSkuId());
        });

        //远程调用库存系统
        Resp<List<SkuStockVo>> infos = wmsSkuFeignService.skusWareInfos(skuIds);
        List<SkuStockVo> skuStockVos = infos.getData();
        //根据spuId查出对应的属性
        List<ProductAttrValueEntity> spuAttrs = spuAttrValueDao.selectList(new QueryWrapper<ProductAttrValueEntity>().eq("spu_id", spuId));
        List<Long> attrIds = new ArrayList<>();
        spuAttrs.forEach(attr ->{
            attrIds.add(attr.getAttrId());
        });
        //将可以检索的属性 过滤出来
        //pms_attr表中search_type=1表示可以检索
        List<AttrEntity> attrEntities = attrDao.selectList(new QueryWrapper<AttrEntity>().in("attr_id", attrIds).eq("search_type", 1));
        //将检索出来的属性字段 封装到es中进行保存
        List<EsSkuAttributeValue> esSkuAttrs = new ArrayList<>();
        attrEntities.forEach(attr ->{
            //通过spuId查出的spu属性中存在search_type=0的数据 需要进行过滤
            spuAttrs.forEach(spuAttr ->{

                if (spuAttr.getAttrId() == attr.getAttrId()){
                    //说明这里就是search_type=1的相关数据
                    EsSkuAttributeValue esSkuAttributeValue = new EsSkuAttributeValue();
                    esSkuAttributeValue.setId(spuAttr.getId());
                    esSkuAttributeValue.setProductAttributeId(spuAttr.getAttrId());
                    esSkuAttributeValue.setName(spuAttr.getAttrName());
                    esSkuAttributeValue.setValue(spuAttr.getAttrValue());
                    esSkuAttributeValue.setSpuId(spuId);
                    esSkuAttrs.add(esSkuAttributeValue);
                }

            });

        });

        //将商品的属性对象封装等esvo
        if (skus != null && skus.size() >0){
            skus.forEach(sku ->{
                EsSkuVo esSkuVo = skuInfoToEsSkuVo(sku,brandEntity,categoryEntity,skuStockVos,esSkuAttrs);
                esSkuVoList.add(esSkuVo);
            });
        }

        //远程调用es 如果上架成功
        Resp<Object> resp = esSkuFeignService.spuUp(esSkuVoList);
        if (resp.getCode() ==0){
            //说明远程到es成功 将spu的状态修改
            SpuInfoEntity entity = new SpuInfoEntity();
            entity.setId(spuId);
            entity.setPublishStatus(status);
            entity.setUodateTime(new Date());
            spuInfoDao.updateById(entity);
        }

    }

    //将商品的属性对象封装等esvo
    private EsSkuVo skuInfoToEsSkuVo(SkuInfoEntity sku, BrandEntity brandEntity, CategoryEntity categoryEntity, List<SkuStockVo> skuStockVos, List<EsSkuAttributeValue> esSkuAttrs) {

        EsSkuVo esSkuVo = new EsSkuVo();
        esSkuVo.setId(sku.getSkuId());
        if (brandEntity != null){
            esSkuVo.setBrandId(brandEntity.getBrandId());
            esSkuVo.setBrandName(brandEntity.getName());
        }
        if (categoryEntity != null){
            esSkuVo.setProductCategoryId(categoryEntity.getCatId());
            esSkuVo.setProductCategoryName(categoryEntity.getName());
        }
        esSkuVo.setPic(sku.getSkuDefaultImg());
        esSkuVo.setName(sku.getSkuTitle());
        esSkuVo.setPrice(sku.getPrice());
        //后面需要进行深入
        esSkuVo.setSale(0);
        esSkuVo.setSort(0);

        skuStockVos.forEach(item ->{

            if (sku.getSkuId() == item.getSkuId()){
                esSkuVo.setStock(item.getStock());
            }

        });

        esSkuVo.setAttrValueList(esSkuAttrs);

        return esSkuVo;
    }

}