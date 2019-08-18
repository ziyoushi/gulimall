package com.atguigu.gulimall.pms.service.impl;

import com.atguigu.gulimall.pms.entity.SkuImagesEntity;
import com.atguigu.gulimall.pms.entity.SkuInfoEntity;
import com.atguigu.gulimall.pms.entity.SpuInfoDescEntity;
import com.atguigu.gulimall.pms.service.ItemService;
import com.atguigu.gulimall.pms.service.SkuImagesService;
import com.atguigu.gulimall.pms.service.SkuInfoService;
import com.atguigu.gulimall.pms.vo.SkuItemDetailVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Administrator
 * @create 2019-08-18 19:47
 */
@Service
public class ItemServiceImpl implements ItemService {


    @Autowired
    @Qualifier("mainThreadPool")
    ThreadPoolExecutor executor;

    @Autowired
    private SkuInfoService skuInfoService;

    @Autowired
    private SkuImagesService skuImagesService;

    @Autowired
    private SpuInfoDescServiceImpl spuInfoDescService;


    //使用异步调用 异步编排的方式
    @Override
    public SkuItemDetailVo getDetails(Long skuId) throws ExecutionException, InterruptedException {

        SkuItemDetailVo detailVo = new SkuItemDetailVo();

        //todo //1、当前sku的基本信息 skuInfo
        CompletableFuture<SkuInfoEntity> skuInfo = CompletableFuture.supplyAsync(() -> {
            SkuInfoEntity sku = skuInfoService.getById(skuId);
            return sku;
        }, executor);

        CompletableFuture<Void> skuInfoAsync = skuInfo.thenAcceptAsync((item) -> {
            BeanUtils.copyProperties(item, detailVo);
        }, executor);

        //todo //2、sku的所有图片
        CompletableFuture<List<SkuImagesEntity>> images = CompletableFuture.supplyAsync(() -> {

            List<SkuImagesEntity> skuImges = skuImagesService.list(new QueryWrapper<SkuImagesEntity>().eq("sku_id", skuId));
            return skuImges;

        }, executor);

        CompletableFuture<Void> imgAsync = images.thenAcceptAsync((imgs) -> {
            List<String> imgList = new ArrayList<>();
            for (SkuImagesEntity image : imgs) {
                String imgUrl = image.getImgUrl();
                imgList.add(imgUrl);
            }
            detailVo.setPics(imgList);

        }, executor);

        //todo //3、sku的所有促销信息

        //todo //4、sku的所有销售属性组合

        //todo//5、spu的所有基本属性

        //todo//6、详情介绍 spu_des
        CompletableFuture<Void> spuDescAsync = skuInfo.thenAcceptAsync((item) -> {
            Long spuId = item.getSpuId();
            SpuInfoDescEntity spuDesc = spuInfoDescService.getById(spuId);
            detailVo.setDesc(spuDesc);
        }, executor);

        //最后阻塞 异步编排获取结果后 封装到detailVo
        CompletableFuture<Void> future = CompletableFuture.allOf(skuInfo,skuInfoAsync,images, imgAsync, spuDescAsync);

        future.get();
        return detailVo;
    }
}
