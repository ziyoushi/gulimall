package com.atguigu.gulimall.pms.vo;

import com.atguigu.gulimall.pms.entity.CategoryEntity;
import lombok.Data;

import java.util.List;

/**
 * @author Administrator
 * @create 2019-08-12 10:05
 */
@Data
public class CategoryWithChildrensVo extends CategoryEntity {

    private List<CategoryEntity> subs;
}
