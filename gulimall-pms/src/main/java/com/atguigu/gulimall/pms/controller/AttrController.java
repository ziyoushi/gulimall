package com.atguigu.gulimall.pms.controller;

import com.atguigu.gulimall.commons.bean.PageVo;
import com.atguigu.gulimall.commons.bean.QueryCondition;
import com.atguigu.gulimall.commons.bean.Resp;
import com.atguigu.gulimall.pms.entity.AttrEntity;
import com.atguigu.gulimall.pms.entity.AttrGroupEntity;
import com.atguigu.gulimall.pms.service.AttrGroupService;
import com.atguigu.gulimall.pms.service.AttrService;
import com.atguigu.gulimall.pms.vo.AttrSaveVo;
import com.atguigu.gulimall.pms.vo.AttrWithGroupVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;




/**
 * 商品属性
 *
 * @author changchen
 * @email doublechang123@163.com
 * @date 2019-08-01 23:59:57
 */
@Api(tags = "商品属性 管理")
@RestController
@RequestMapping("pms/attr")
public class AttrController {
    @Autowired
    private AttrService attrService;

    @Autowired
    private AttrGroupService attrGroupService;


    /**
     * 查询基本属性
     * @param queryCondition
     * @param catId
     * @return
     */
    @ApiOperation("查询某个三级分类下的所有基本属性")
    @GetMapping("/base/{catId}")
    public Resp<PageVo> attrBase(QueryCondition queryCondition,
                                 @PathVariable("catId") Long catId){

        PageVo pageVo = attrService.queryPageCategoryBaseAttrs(queryCondition,catId,1);

        return Resp.ok(pageVo);
    }

    @ApiOperation("查询某个三级分类下的所有基本属性")
    @GetMapping("/sale/{catId}")
    public Resp<PageVo> attrSale(QueryCondition queryCondition,
                                 @PathVariable("catId") Long catId){

        PageVo pageVo = attrService.queryPageCategoryBaseAttrs(queryCondition,catId,0);

        return Resp.ok(pageVo);
    }

    /**
     * 列表
     */
    @ApiOperation("分页查询(排序)")
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('pms:attr:list')")
    public Resp<PageVo> list(QueryCondition queryCondition) {
        PageVo page = attrService.queryPage(queryCondition);

        return Resp.ok(page);
    }


    /**
     * 信息
     */
    @ApiOperation("详情查询")
    @GetMapping("/info/{attrId}")
    @PreAuthorize("hasAuthority('pms:attr:info')")
    public Resp<AttrWithGroupVo> info(@PathVariable("attrId") Long attrId){
		AttrEntity attr = attrService.getById(attrId);

        AttrWithGroupVo attrWithGroupVo = new AttrWithGroupVo();
        BeanUtils.copyProperties(attr,attrWithGroupVo);

        //根据属性id查询属性分组
        AttrGroupEntity attrGroupEntity = attrGroupService.getGroupInfoByAttrId(attrId);
        attrWithGroupVo.setGroup(attrGroupEntity);

        return Resp.ok(attrWithGroupVo);
    }

    /**
     * 保存
     */
    @ApiOperation("保存")
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('pms:attr:save')")
    public Resp<Object> save(@RequestBody AttrSaveVo attr){
		attrService.saveAttrAndRelation(attr);
        return Resp.ok(null);
    }

    /**
     * 修改
     */
    @ApiOperation("修改")
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('pms:attr:update')")
    public Resp<Object> update(@RequestBody AttrEntity attr){
		attrService.updateById(attr);

        return Resp.ok(null);
    }

    /**
     * 删除
     */
    @ApiOperation("删除")
    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('pms:attr:delete')")
    public Resp<Object> delete(@RequestBody Long[] attrIds){
		attrService.removeByIds(Arrays.asList(attrIds));

        return Resp.ok(null);
    }

}
