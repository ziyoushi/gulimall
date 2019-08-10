package com.atguigu.gulimall.search.controller;

import com.atguigu.gulimall.commons.bean.Resp;
import io.searchbox.client.JestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public Resp<Object> spuUp(){

        return Resp.ok(null);
    }

}
