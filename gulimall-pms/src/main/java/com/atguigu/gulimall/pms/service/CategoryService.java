package com.atguigu.gulimall.pms.service;

import com.atguigu.gulimall.pms.vo.CategoryWithChildrensVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.gulimall.pms.entity.CategoryEntity;
import com.atguigu.gulimall.commons.bean.PageVo;
import com.atguigu.gulimall.commons.bean.QueryCondition;

import java.util.List;


/**
 * 商品三级分类
 *
 * @author changchen
 * @email doublechang123@163.com
 * @date 2019-08-01 23:59:57
 */
public interface CategoryService extends IService<CategoryEntity> {

    PageVo queryPage(QueryCondition params);

    List<CategoryEntity> getCategoryTree(Integer level);

    List<CategoryEntity> getCategoryChildrenTree(Integer catId);

    List<CategoryWithChildrensVo> selectCategoryChildrenWithChildrens(long id);


}

