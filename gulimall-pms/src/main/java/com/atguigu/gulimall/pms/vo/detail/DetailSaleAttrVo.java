package com.atguigu.gulimall.pms.vo.detail;

import lombok.Data;

/**
 * @author Administrator
 * @create 2019-08-18 19:43
 */
@Data
public class DetailSaleAttrVo {
    private Long attrId;//销售属性的id
    private String attrName;//销售属性的名字
    private String[] attrValues;
}
