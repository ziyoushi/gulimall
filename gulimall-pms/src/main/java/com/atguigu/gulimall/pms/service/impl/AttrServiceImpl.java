package com.atguigu.gulimall.pms.service.impl;

import com.atguigu.gulimall.commons.bean.PageVo;
import com.atguigu.gulimall.commons.bean.Query;
import com.atguigu.gulimall.commons.bean.QueryCondition;
import com.atguigu.gulimall.pms.dao.AttrAttrgroupRelationDao;
import com.atguigu.gulimall.pms.dao.AttrDao;
import com.atguigu.gulimall.pms.entity.AttrAttrgroupRelationEntity;
import com.atguigu.gulimall.pms.entity.AttrEntity;
import com.atguigu.gulimall.pms.service.AttrService;
import com.atguigu.gulimall.pms.vo.AttrSaveVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {

    @Autowired
    private AttrAttrgroupRelationDao relationDao;
    
    @Autowired
    private AttrDao attrDao;
    
    @Override
    public PageVo queryPage(QueryCondition params) {
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                new QueryWrapper<AttrEntity>()
        );

        return new PageVo(page);
    }

    @Override
    public PageVo queryPageCategoryBaseAttrs(QueryCondition queryCondition, Long catId, Integer attrType) {

        //封装查询条件
        QueryWrapper<AttrEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("catelog_id",catId);
        queryWrapper.eq("attr_type",attrType);

        IPage<AttrEntity> page = new Query<AttrEntity>().getPage(queryCondition);

        IPage<AttrEntity> data = this.page(page, queryWrapper);

        return new PageVo(data);

    }

    @Transactional
    @Override
    public void saveAttrAndRelation(AttrSaveVo attr) {

        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attr,attrEntity);
        attrDao.insert(attrEntity);

        Long attrId = attrEntity.getAttrId();

        AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();

        relationEntity.setAttrGroupId(attr.getAttrGroupId());
        relationEntity.setAttrId(attrId);
        relationDao.insert(relationEntity);
    }

}