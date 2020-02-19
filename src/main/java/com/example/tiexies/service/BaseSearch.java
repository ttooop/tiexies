package com.example.tiexies.service;

import cn.hutool.core.util.StrUtil;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
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

    protected BoolQueryBuilder queryStringQueryBuilder(String content,String... disabledtable){
        QueryStringQueryBuilder queryBuilder= QueryBuilders
                .queryStringQuery(content)
                .defaultOperator(Operator.AND)
                .fuzzyTranspositions(false);
        BoolQueryBuilder disboolQueryBuilder=QueryBuilders.boolQuery();
        for (int i = 0; i < disabledtable.length; i++) {
            disboolQueryBuilder.mustNot(QueryBuilders.matchQuery("tiexi_table_name",disabledtable[i]));
        }

        BoolQueryBuilder boolQueryBuilder=QueryBuilders
                .boolQuery().must(queryBuilder).must(disboolQueryBuilder);
        return boolQueryBuilder;
    }
    protected BoolQueryBuilder newquery(String content,String ... disabledtable){
        QueryStringQueryBuilder queryBuilder= QueryBuilders
                .queryStringQuery(content)
                .defaultOperator(Operator.AND)
                .fuzzyTranspositions(false);

        BoolQueryBuilder disboolQueryBuilder=QueryBuilders.boolQuery();
        for (int i = 0; i < disabledtable.length; i++) {
            disboolQueryBuilder.mustNot(QueryBuilders.matchQuery("tiexi_table_name",disabledtable[i]));
        }

        BoolQueryBuilder boolQueryBuilder=QueryBuilders
                .boolQuery().must(queryBuilder).must(disboolQueryBuilder);
        return boolQueryBuilder;
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
