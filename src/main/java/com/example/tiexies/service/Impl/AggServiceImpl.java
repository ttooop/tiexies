package com.example.tiexies.service.Impl;


import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.example.tiexies.entity.AggDBVO;
import com.example.tiexies.entity.Result;
import com.example.tiexies.service.AggService;
import com.example.tiexies.service.BaseSearch;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Service;
import sun.util.resources.ga.LocaleNames_ga;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Slf4j
public class AggServiceImpl extends BaseSearch implements AggService {


    /**
     * 该方法要实现两部分工作，
     * 一是按照聚合查询，将新闻类数据和项目类数据通过聚合搜索，分别返回数据和他们搜索出的条数
     *      该过程考虑到要实现两部分功能，一个是新闻类的聚合（根据categoryid），一个是项目类的聚合（根据tiexi_db_name）
     * 二是返回全文搜索搜出的项目名的列表集合（该过程的实现直接通过query_string进行全文检索，返回固定的字段即可）
     * @param content
     * @param curpage
     * @param pagesize
     * @return
     * @throws IOException
     */
    @Override
    public Result aggByDB(String content, int curpage, int pagesize) throws IOException {
        Map<String,Object> result=new HashMap<>();

        content=wordRegular(content);
        Assert.isTrue(StrUtil.isNotBlank(content),"搜索词无效");

        //新闻类聚合
        log.info("search word in news is {}",content);

        //别名索引，表示只对新闻类的数据进行分类聚合查询
        SearchRequest newsrequest = new SearchRequest("news");

        QueryStringQueryBuilder stringQueryBuilder=queryStringQueryBuilder(content);
        //聚合查询
        /**
         * .terms(聚类名)
         * .field(聚类字段)
         */
        TermsAggregationBuilder groupByNews=AggregationBuilders.terms("新闻分类").field("category_id");

        //因为只返回类型和条数，所以size为0，不取内容
        SearchSourceBuilder sourceBuildernews=new SearchSourceBuilder()
                .query(stringQueryBuilder)
                .size(0)
                .aggregation(groupByNews);

        newsrequest.source(sourceBuildernews);
        SearchResponse newsresponse =client.search(newsrequest,RequestOptions.DEFAULT);
        log.info("the news response is {} ",newsresponse);

        Map<String,Aggregation> newsAggMap=newsresponse.getAggregations().asMap();

        Terms newsgroup=(Terms) newsAggMap.get("新闻分类");
        List<Map<String,Object>> newsandnum=new ArrayList<>();
        for (Terms.Bucket buck :
                newsgroup.getBuckets()) {
            String key=buck.getKeyAsString();
            Long count=buck.getDocCount();
            Map<String,Object> tmp=new HashMap<>();
            tmp.put("data",key);
            tmp.put("count",count);
            newsandnum.add(tmp);
        }

        result.put("nesandnum",newsandnum);

        log.info("search word is {}",content);

        SearchRequest request=new SearchRequest("sytxgspt1");

        QueryStringQueryBuilder queryStringQueryBuilder=queryStringQueryBuilder(content);

        TermsAggregationBuilder dbGroupByAGG= AggregationBuilders.terms("数据分类").field("tiexi_db_name");

        SearchSourceBuilder sourceBuilder=new SearchSourceBuilder()
                .query(queryStringQueryBuilder)
                .size(0)
                .aggregation(dbGroupByAGG);

        request.source(sourceBuilder);

        SearchResponse response=client.search(request, RequestOptions.DEFAULT);
        log.info("the response is {}",response);

        Map<String, Aggregation> aggMap=response.getAggregations().asMap();

        Terms dbgroupby=(Terms) aggMap.get("数据分类");
        List<Map<String, Object>> dbandnum=new ArrayList<>();
        for (Terms.Bucket buck :
                dbgroupby.getBuckets()) {
            String key=buck.getKeyAsString();
            Long count=buck.getDocCount();
            Map<String,Object> tmp=new HashMap<>();
            tmp.put("data",key);
            tmp.put("count",count);
            dbandnum.add(tmp);
        }
        //dbandnum.stream().forEach(map->System.out.println(map.toString()));

        result.put("countmap",dbandnum);

        //以上到此为止获取的是按照各种数据类型聚合查询的匹配结果条数的列表包括------数据类型：匹配数

        log.info("info search word is {}",content);

        SearchRequest inforequest=new SearchRequest("sytxgspt1");
        //获取详情列表
        SearchSourceBuilder infosourceBuilder=new SearchSourceBuilder()
                .query(queryStringQueryBuilder)
                .from((curpage-1)*pagesize)
                .size(pagesize);

        inforequest.source(infosourceBuilder);

        SearchResponse inforesponse=client.search(inforequest, RequestOptions.DEFAULT);

        log.info("info search result {}",inforesponse);

        SearchHit[] source = inforesponse.getHits().getHits();

        List<Map<String,Object>> inforesult=new ArrayList<>();
        for (SearchHit s :
                source) {
            Map<String,Object> tmp=s.getSourceAsMap();
            inforesult.add(tmp);
        }

        //inforesult.stream().forEach(s-> System.out.println(s.toString()));

        result.put("searchword",content);
        result.put("totalhits",inforesponse.getHits().getTotalHits());
        result.put("infolist",inforesult);


        return Result.create(result);
    }

    @Override
    public Result typesearchByDB(String indexname, String content, int curpage, int pagesize) throws IOException {

        Map<String,Object> result=new HashMap<>();
        content=wordRegular(content);
        Assert.isTrue(StrUtil.isNotBlank(content),"搜索词无效");

        log.info("search word is {}",content);
        SearchRequest request=new SearchRequest(indexname);

        QueryStringQueryBuilder queryStringQueryBuilder=queryStringQueryBuilder(content);

        SearchSourceBuilder sourceBuilder=new SearchSourceBuilder()
                .query(queryStringQueryBuilder)
                .from((curpage-1)*pagesize)
                .size(pagesize);

        request.source(sourceBuilder);

        SearchResponse response=client.search(request,RequestOptions.DEFAULT);

        SearchHit[] searchHits=response.getHits().getHits();

        List<Map<String,Object>> typeinfo=new ArrayList<>();
        for (SearchHit s :
                searchHits) {
            Map<String,Object> tmp=s.getSourceAsMap();
            typeinfo.add(tmp);
        }

        result.put("searchword",content);
        result.put("totalhits",response.getHits().getTotalHits());
        result.put("infolist",typeinfo);



        return Result.create(result);
    }
}
