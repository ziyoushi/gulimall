package com.atguigu.gulimall.pms.vo;

import com.atguigu.gulimall.pms.entity.SpuInfoEntity;
import lombok.Data;

import java.util.List;

/**
 * @author Administrator
 * @create 2019-08-05 9:43
 */
@Data
public class SpuAllSaveVo  extends SpuInfoEntity {

    /**
     * "spuName": "string", //商品名字
     * "brandId": 0,    //品牌id
     * "catalogId": 0,    //分类id
     * "publishStatus": 0,  //发布状态
     * "spuDescription": "string",  //spu描述
     * "spuImages": [  "string" ], //spu图片
     */
    private String[] spuImages;
    private List<BaseAttrVo> baseAttrs;

    private List<SkusVo> skus;

}
