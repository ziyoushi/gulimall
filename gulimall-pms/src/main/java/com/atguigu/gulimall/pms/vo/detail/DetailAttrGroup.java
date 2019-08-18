package com.atguigu.gulimall.pms.vo.detail;

import lombok.Data;

import java.util.List;

/**
 * @author Administrator
 * @create 2019-08-18 19:45
 */
@Data
public class DetailAttrGroup {
    private Long id;
    private String name;//分组的名字
    private List<DetailBaseAttrVo> attrs;
}
