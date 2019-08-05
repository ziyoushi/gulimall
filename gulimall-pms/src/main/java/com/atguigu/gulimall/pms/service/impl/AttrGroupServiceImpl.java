package com.atguigu.gulimall.pms.service.impl;

import com.atguigu.gulimall.commons.bean.PageVo;
import com.atguigu.gulimall.commons.bean.Query;
import com.atguigu.gulimall.commons.bean.QueryCondition;
import com.atguigu.gulimall.pms.dao.AttrAttrgroupRelationDao;
import com.atguigu.gulimall.pms.dao.AttrDao;
import com.atguigu.gulimall.pms.dao.AttrGroupDao;
import com.atguigu.gulimall.pms.entity.AttrAttrgroupRelationEntity;
import com.atguigu.gulimall.pms.entity.AttrEntity;
import com.atguigu.gulimall.pms.entity.AttrGroupEntity;
import com.atguigu.gulimall.pms.service.AttrGroupService;
import com.atguigu.gulimall.pms.vo.AttrGroupWithAttrsVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    @Autowired
    private AttrAttrgroupRelationDao relationDao;

    @Autowired
    private AttrGroupDao attrGroupDao;

    @Autowired
    private AttrDao attrDao;

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

        List<AttrGroupEntity> records = data.getRecords();


        List<AttrGroupWithAttrsVo> groupWithAttrsVos = new ArrayList<>();

        for (AttrGroupEntity record : records) {
            AttrGroupWithAttrsVo vo = new AttrGroupWithAttrsVo();
            BeanUtils.copyProperties(record,vo);

            Long attrGroupId = record.getAttrGroupId();

            //根据attrGroupId 查出关联关系表
            List<AttrAttrgroupRelationEntity> relationEntities = relationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>()
                    .eq("attr_group_id", attrGroupId));

            if (relationEntities != null && relationEntities.size()>0){

                List<Long> attrIds = new ArrayList<>();
                for (AttrAttrgroupRelationEntity entity : relationEntities) {
                    Long attrId = entity.getAttrId();
                    attrIds.add(attrId);
                }
                List<AttrEntity> attrEntities = attrDao.selectList(new QueryWrapper<AttrEntity>().in("attr_id", attrIds));

                vo.setAttrEntities(attrEntities);

            }

            groupWithAttrsVos.add(vo);
        }

        return new PageVo(groupWithAttrsVos,data.getTotal(),data.getSize(),data.getCurrent());


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