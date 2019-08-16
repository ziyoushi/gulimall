package com.atguigu.gulimall.pms.service.impl;

import com.alibaba.fastjson.JSON;
import com.atguigu.gulimall.commons.bean.Constant;
import com.atguigu.gulimall.commons.bean.PageVo;
import com.atguigu.gulimall.commons.bean.Query;
import com.atguigu.gulimall.commons.bean.QueryCondition;
import com.atguigu.gulimall.pms.dao.CategoryDao;
import com.atguigu.gulimall.pms.entity.CategoryEntity;
import com.atguigu.gulimall.pms.service.CategoryService;
import com.atguigu.gulimall.pms.vo.CategoryWithChildrensVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Slf4j
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Autowired
    private CategoryDao categoryDao;

    private StringRedisTemplate redisTemplate;

    @Override
    public PageVo queryPage(QueryCondition params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageVo(page);
    }

    @Override
    public List<CategoryEntity> getCategoryTree(Integer level) {

        QueryWrapper<CategoryEntity> queryWrapper = new QueryWrapper<>();
        if (level != 0){
            queryWrapper.eq("cat_level",level);

        }
        List<CategoryEntity> list = categoryDao.selectList(queryWrapper);

        return list;
    }

    @Override
    public List<CategoryEntity> getCategoryChildrenTree(Integer catId) {

        QueryWrapper<CategoryEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_cid",catId);

        List<CategoryEntity> list = categoryDao.selectList(queryWrapper);

        return list;
    }

    @Override
    public List<CategoryWithChildrensVo> selectCategoryChildrenWithChildrens(long id) {

        List<CategoryWithChildrensVo> vos = null;

        String s = redisTemplate.opsForValue().get(Constant.CACHE_CATELOG);
        if (!StringUtils.isEmpty(s)){
            log.debug("缓存命中");
            vos = JSON.parseArray(s, CategoryWithChildrensVo.class);
        }else {

            //缓存中没有从数据库中查询
            vos = categoryDao.selectCategoryChildrenWithChildrens(id);
            //在放入缓存中
            redisTemplate.opsForValue().set(Constant.CACHE_CATELOG,JSON.toJSONString(vos));
        }

        return vos;
    }

}