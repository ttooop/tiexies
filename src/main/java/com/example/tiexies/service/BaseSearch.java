package com.example.tiexies.service;

import cn.hutool.core.util.StrUtil;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;

import java.util.Arrays;
import java.util.stream.Collectors;

public class BaseSearch {

    @Qualifier("restHighLevelClient")
    @Autowired
    protected RestHighLevelClient client;

    @Value("${elasticsearch.index-prefix}")
    protected String indexPrefix;

    @Value("${elasticsearch.alias-prefix}")
    protected String aliasPrefix;

    protected QueryStringQueryBuilder queryStringQueryBuilder(String content){
        QueryStringQueryBuilder queryBuilder= QueryBuilders
                .queryStringQuery(content)
                .defaultOperator(Operator.AND)
                .fuzzyTranspositions(false);
        return queryBuilder;
    }

    /**
     * 规范化搜索词
     * @param word
     * @return
     */
    protected String wordRegular(String word){
        word=word.replaceAll("[\\pP\\p{Punct}]"," ");
        String[] words= StrUtil.trimToEmpty(word).split("\\s+");
        return Arrays.stream(words).collect(Collectors.joining(" AND "));
    }
}
