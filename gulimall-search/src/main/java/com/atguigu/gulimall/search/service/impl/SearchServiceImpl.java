package com.atguigu.gulimall.search.service.impl;

import com.atguigu.gulimall.commons.bean.Constant;
import com.atguigu.gulimall.commons.to.es.EsSkuVo;
import com.atguigu.gulimall.search.service.SearchService;
import com.atguigu.gulimall.search.vo.SearchParam;
import com.atguigu.gulimall.search.vo.SearchResponse;
import com.atguigu.gulimall.search.vo.SearchResponseAttrVo;
import io.searchbox.client.JestClient;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.core.search.aggregation.ChildrenAggregation;
import io.searchbox.core.search.aggregation.MetricAggregation;
import io.searchbox.core.search.aggregation.TermsAggregation;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.support.ValueType;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 * @create 2019-08-11 19:12
 */
@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    JestClient jestClient;

    /**
     * 从es系统中检索出商品
     *
     * @param param
     * @return
     */
    @Override
    public SearchResponse search(SearchParam param) {

        //将前端传递的参数构件成dsl
        String query = buildDSL(param);

        //创建检索索引
        Search build = new Search.Builder(query)
                .addIndex(Constant.ES_SPU_INDEX)
                .addType(Constant.ES_SPU_TYPE)
                .build();

        //执行
        try {
            SearchResult result = jestClient.execute(build);
            //根据result构建SearchResponse
            SearchResponse response = buildResult(result);

            //封装前端传的页码 每页数
            response.setPageNum(param.getPageNum());
            response.setPageSize(param.getPageSize());

            return response;

        } catch (IOException e) {

        }

        return null;
    }

    //根据dsl查询的结果进行数据重组封装
    private SearchResponse buildResult(SearchResult result) {

        System.out.println(result);

        SearchResponse searchResponse = new SearchResponse();
        //从返回的结果中抽取数据
        List<SearchResult.Hit<EsSkuVo, Void>> hits = result.getHits(EsSkuVo.class);
        List<EsSkuVo> esSkuVos = new ArrayList<>();
        hits.forEach( hit ->{
            EsSkuVo source = hit.source;
            esSkuVos.add(source);
        });

        searchResponse.setProducts(esSkuVos);

        //封装分页的总记录数
        searchResponse.setTotal(result.getTotal());

        //所有的聚合结果
        MetricAggregation aggregations = result.getAggregations();

        ChildrenAggregation attr_agg = aggregations.getChildrenAggregation("attr_agg");
        TermsAggregation attrId_agg = attr_agg.getTermsAggregation("attrId_agg");
        List<SearchResponseAttrVo> attrs = new ArrayList<>();
        //获取到attrId_agg的Bucket就能知道有多少个attrId
        attrId_agg.getBuckets().forEach(bucket ->{

            Long attrId = Long.parseLong(bucket.getKey());
            TermsAggregation attrName_agg = bucket.getTermsAggregation("attrName_agg");
            TermsAggregation attrValue_agg = bucket.getTermsAggregation("attrValue_agg");

            //属性名
            String attrName = attrName_agg.getBuckets().get(0).getKey();

            //属性值
            List<String> attrValues = new ArrayList<>();
            attrValue_agg.getBuckets().forEach(attrValueBucket->{
                String value = attrValueBucket.getKey();
                attrValues.add(value);
            });

            //构造一个属性对象
            SearchResponseAttrVo attrVo = new SearchResponseAttrVo();
            attrVo.setProductAttributeId(attrId);
            attrVo.setName(attrName);
            attrVo.setValue(attrValues);

            attrs.add(attrVo);
        });

        searchResponse.setAttrs(attrs);

        SearchResponseAttrVo brand = new SearchResponseAttrVo();

        searchResponse.setBrand(brand);

        SearchResponseAttrVo catelog = new SearchResponseAttrVo();

        searchResponse.setCatelog(catelog);


        return searchResponse;
    }

    //根据前端传递的参数构建 dsl
    private String buildDSL(SearchParam param) {

        //创建SearchSourceBuilder
        SearchSourceBuilder builder = new SearchSourceBuilder();

        //根据dsl语法依次构建相关对象
        BoolQueryBuilder bool = new BoolQueryBuilder();

        if (!StringUtils.isEmpty(param.getKeyword())) {
            MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("name", param.getKeyword());
            bool.must(matchQueryBuilder);
        }

        if (param.getBrand() != null && param.getBrand().length >0){
            TermsQueryBuilder brand = new TermsQueryBuilder("brandId",param.getBrand());
            bool.filter(brand);
        }

        if (param.getCatelog3() != null && param.getCatelog3().length >0){
            TermsQueryBuilder category = new TermsQueryBuilder("productCategoryId", param.getCatelog3());
            bool.filter(category);
        }

        //按照价格区间来进行检索
        if (param.getPriceFrom() != null || param.getPriceTo() != null){

            RangeQueryBuilder range = new RangeQueryBuilder("price");
            if (param.getPriceFrom() != null){
                range.gt(param.getPriceFrom());
            }
            if (param.getPriceTo() != null){
                range.lte(param.getPriceTo());
            }

            bool.filter(range);
        }

        //封装组合数组 属性组合关系
        if (param.getProps() != null && param.getProps().length >0){
            //2:win10-android-
            //3:4g
            //4:5.5
            //prop的结构如上
            for (String prop : param.getProps()) {
                String[] split = prop.split(":");
                if (split != null && split.length ==2){
                    String attrId = split[0];
                    String[] attrValues = split[1].split("-");

                    BoolQueryBuilder query = new BoolQueryBuilder();
                    TermQueryBuilder termAttrId = new TermQueryBuilder("attrValueList.productAttributeId",attrId);
                    query.must(termAttrId);

                    TermsQueryBuilder termAttValues = new TermsQueryBuilder("attrValueList.value", attrValues);
                    query.must(termAttValues);

                    NestedQueryBuilder nest = new NestedQueryBuilder("attrValueList",query, ScoreMode.None);
                    bool.filter(nest);

                }

            }

        }

        builder.query(bool);

        //分页
        builder.from((param.getPageNum()-1)*param.getPageSize());
        builder.size(param.getPageSize());

        //高亮
        if (!StringUtils.isEmpty(param.getKeyword())){
            //有检索关键字 需要高亮
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder
                    .preTags("<b style='color:red'>")
                    .postTags("</b>")
                    .field("name");
            builder.highlighter(highlightBuilder);
        }

        //排序
        String order = param.getOrder();
        if (!StringUtils.isEmpty(order)){
            //order=1:asc  0：综合排序  1：销量  2：价格
            String[] split = order.split(":");
            if (split != null && split.length == 2){
                //检索排序规则 升序 降序
                SortOrder sortOrder = split[1].equalsIgnoreCase("asc")?SortOrder.ASC:SortOrder.DESC;
                if (split[0].equals("0")){
                    builder.sort("_score",sortOrder);
                }
                if (split[0].equals("1")){
                    builder.sort("sale",sortOrder);
                }
                if (split[0].equals("2")){
                    builder.sort("price",sortOrder);
                }

            }
        }

        //聚合
        //属性嵌套聚合
        NestedAggregationBuilder attrAgg = new NestedAggregationBuilder("attr_agg","attrValueList");

        //attrId_agg 聚合

        //子聚合
        TermsAggregationBuilder attrId_agg = new TermsAggregationBuilder("attrId_agg", ValueType.LONG);
        attrId_agg.field("attrValueList.productAttributeId");

        //子聚合里的子聚合
        TermsAggregationBuilder attrName = new TermsAggregationBuilder("attrName", ValueType.STRING);
        attrName.field("attrValueList.name");
        TermsAggregationBuilder attrValue = new TermsAggregationBuilder("attrValue", ValueType.STRING);
        attrValue.field("attrValueList.value");

        attrId_agg.subAggregation(attrName);
        attrId_agg.subAggregation(attrValue);

        attrAgg.subAggregation(attrId_agg);

        builder.aggregation(attrAgg);

        //品牌嵌套聚合
        TermsAggregationBuilder brandAgg = new TermsAggregationBuilder("brandId_agg", ValueType.LONG);
        brandAgg.field("brandId");

        //品牌Name子聚合
        TermsAggregationBuilder brandName_agg = new TermsAggregationBuilder("brandName_agg", ValueType.STRING);
        brandName_agg.field("brandName");
        brandAgg.subAggregation(brandName_agg);

        builder.aggregation(brandAgg);

        //分类 嵌套聚合
        TermsAggregationBuilder catelogAgg = new TermsAggregationBuilder("catelog_agg", ValueType.LONG);
        catelogAgg.field("productCategoryId");

        //分类Name子聚合
        TermsAggregationBuilder catelogName_agg = new TermsAggregationBuilder("catelogName_agg", ValueType.STRING);
        catelogName_agg.field("productCategoryName");
        catelogAgg.subAggregation(catelogName_agg);

        builder.aggregation(catelogAgg);

        return builder.toString();
    }
}
