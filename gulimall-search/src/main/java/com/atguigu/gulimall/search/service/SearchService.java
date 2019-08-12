package com.atguigu.gulimall.search.service;

import com.atguigu.gulimall.search.vo.SearchParam;
import com.atguigu.gulimall.search.vo.SearchResponse;

/**
 * @author Administrator
 * @create 2019-08-11 19:11
 */
public interface SearchService {

    //es检索
    SearchResponse search(SearchParam param);
}
