package com.atguigu.gulimall.pms.vo;

import com.atguigu.gulimall.pms.entity.AttrEntity;
import com.atguigu.gulimall.pms.entity.AttrGroupEntity;
import lombok.Data;

/**
 * @author Administrator
 * @create 2019-08-04 15:37
 */
@Data
public class AttrWithGroupVo extends AttrEntity {

    private AttrGroupEntity group;
}
