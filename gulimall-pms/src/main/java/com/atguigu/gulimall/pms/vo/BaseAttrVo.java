package com.atguigu.gulimall.pms.vo;

import lombok.Data;

/**
 * @author Administrator
 * @create 2019-08-05 12:13
 */
@Data
public class BaseAttrVo {
    /**
     * "attrId": 0,   //属性id
     *  "attrName": "string",  //属性名
     *  "valueSelected": [ "string" ] //属性值
     */
    private Long attrId;
    private String attrName;
    private String[] valueSelected;
}
