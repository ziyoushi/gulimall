package com.atguigu.gulimall.pms.dao;

import com.atguigu.gulimall.pms.entity.CategoryEntity;
import com.atguigu.gulimall.pms.vo.CategoryWithChildrensVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 商品三级分类
 * 
 * @author changchen
 * @email doublechang123@163.com
 * @date 2019-08-01 23:59:57
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {


    List<CategoryWithChildrensVo> selectCategoryChildrenWithChildrens(long id);
}
