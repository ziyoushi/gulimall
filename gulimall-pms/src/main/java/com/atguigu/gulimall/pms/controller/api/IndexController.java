package com.atguigu.gulimall.pms.controller.api;

import com.atguigu.gulimall.commons.bean.Resp;
import com.atguigu.gulimall.pms.service.CategoryService;
import com.atguigu.gulimall.pms.vo.CategoryWithChildrensVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Administrator
 * @create 2019-08-12 10:06
 */
@RestController
@RequestMapping("/index")
public class IndexController {

    @Autowired
    private CategoryService categoryService;

    @ApiOperation("查询所有的一级分类的子分类")
    @GetMapping("/cates")
    public Resp<Object> getIndexCates(){

        List<CategoryWithChildrensVo> data = categoryService.selectCategoryChildrenWithChildrens(1L);

        return Resp.ok(data);
    }

}
