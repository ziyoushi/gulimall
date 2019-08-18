package com.atguigu.gulimall.pms.vo.detail;

import lombok.Data;

/**
 * @author Administrator
 * @create 2019-08-18 19:45
 */
@Data
public class CouponsVo {
    private String name;//促销信息/优惠券的名字
    // 0-优惠券    1-满减    2-阶梯
    private Integer type;
}
