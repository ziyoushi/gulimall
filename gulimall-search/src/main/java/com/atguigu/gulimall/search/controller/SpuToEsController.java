package com.atguigu.gulimall.search.controller;

import com.atguigu.gulimall.commons.bean.Constant;
import com.atguigu.gulimall.commons.bean.Resp;
import com.atguigu.gulimall.commons.to.es.EsSkuVo;
import io.searchbox.client.JestClient;
import io.searchbox.core.Index;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Administrator
 * @create 2019-08-10 15:31
 */
@RestController
@RequestMapping("/es")
public class SpuToEsController {

    @Autowired
    private JestClient jestClient;

    @PostMapping("/spu/up")
    public Resp<Object> spuUp(@RequestBody List<EsSkuVo> vos){

        System.out.println("远程传递过来的---》"+vos);
        vos.forEach(vo ->{
            Index index = new Index.Builder(vo)
                    .index(Constant.ES_SPU_INDEX)
                    .type(Constant.ES_SPU_TYPE)
                    .id(vo.getId().toString())
                    .build();
            try {
                jestClient.execute(index);
            }catch (Exception e){

            }
        });

        return Resp.ok(null);
    }

}
