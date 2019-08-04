package com.atguigu.gulimall.pms.service.impl;

import com.atguigu.gulimall.commons.bean.PageVo;
import com.atguigu.gulimall.commons.bean.Query;
import com.atguigu.gulimall.commons.bean.QueryCondition;
import com.atguigu.gulimall.pms.dao.AttrAttrgroupRelationDao;
import com.atguigu.gulimall.pms.dao.AttrGroupDao;
import com.atguigu.gulimall.pms.entity.AttrAttrgroupRelationEntity;
import com.atguigu.gulimall.pms.entity.AttrGroupEntity;
import com.atguigu.gulimall.pms.service.AttrGroupService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    @Autowired
    private AttrAttrgroupRelationDao relationDao;

    @Autowired
    private AttrGroupDao attrGroupDao;


    @Override
    public PageVo queryPage(QueryCondition params) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<AttrGroupEntity>()
        );

        return new PageVo(page);
    }

    @Override
    public PageVo queryPageGroupByGroupId(QueryCondition queryCondition, Long catId) {

        IPage<AttrGroupEntity> page = new Query<AttrGroupEntity>().getPage(queryCondition);

        QueryWrapper<AttrGroupEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("catelog_id",catId);

        IPage<AttrGroupEntity> data = this.page(page, queryWrapper);

        return new PageVo(data);

    }

    @Override
    public AttrGroupEntity getGroupInfoByAttrId(Long attrId) {

        AttrGroupEntity attrGroupEntity = null;

        AttrAttrgroupRelationEntity relationEntity = relationDao.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>()
                .eq("attr_id", attrId));

        if (relationEntity != null){

            Long attrGroupId = relationEntity.getAttrGroupId();
            attrGroupEntity = attrGroupDao.selectById(attrGroupId);
        }

        return attrGroupEntity;

    }

}