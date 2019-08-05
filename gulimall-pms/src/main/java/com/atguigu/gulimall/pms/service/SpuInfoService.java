package com.atguigu.gulimall.pms.service;

import com.atguigu.gulimall.pms.vo.SkusVo;
import com.atguigu.gulimall.pms.vo.SpuAllSaveVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.gulimall.pms.entity.SpuInfoEntity;
import com.atguigu.gulimall.commons.bean.PageVo;
import com.atguigu.gulimall.commons.bean.QueryCondition;

import java.util.List;


/**
 * spu信息
 *
 * @author changchen
 * @email doublechang123@163.com
 * @date 2019-08-01 23:59:57
 */
public interface SpuInfoService extends IService<SpuInfoEntity> {

    PageVo queryPage(QueryCondition params);

    PageVo queryPageByCatId(QueryCondition queryCondition, Long catId);

    void saveAllSpuBase(SpuAllSaveVo spuInfo);

    //保存spu基本信息
    Long saveSpuBaseInfo(SpuAllSaveVo spuInfo);

    //保存spu图片信息
    void saveSpuInfoDesc(Long spuId, SpuAllSaveVo spuInfo);

    //保存spu基本属性
    void saveSpuBaseAttrs(Long spuId, SpuAllSaveVo spuInfo);

    //保存sku基本信息
    void saveSkuInfo(Long spuId, List<SkusVo> skus);
}
