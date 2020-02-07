package com.example.tiexies.service.Impl;

import com.example.tiexies.entity.ESIndexDTO;
import com.example.tiexies.service.BaseSearch;
import com.example.tiexies.service.IndexService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.alias.Alias;
import org.elasticsearch.action.admin.indices.alias.IndicesAliasesRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static com.example.tiexies.Indexenum.IndexEnum.DOC;

@Service
@Slf4j
public class IndexServiceImpl extends BaseSearch implements IndexService {

    @Qualifier("restHighLevelClient")
    @Autowired
    private RestHighLevelClient client;

    @Value("${elasticsearch.index-prefix}")
    private String indexPrefix;

    @Value("${elasticsearch.alias-prefix}")
    private String aliasPrefix;


    @Override
    public void createIndex(ESIndexDTO esIndexDTO) throws IOException {

        CreateIndexRequest request=new CreateIndexRequest(esIndexDTO.getIndexName());

        request.settings(Settings.builder()
                .put("index.number_of_shards",esIndexDTO.getNumShards())
                .put("index.number_of_replicas",1)
                .put("index.refresh_interval","60s"));

        //初始化索引配置
        request.mapping("doc",DOC.getDescription(), XContentType.JSON);

        //设置别名
        request.alias(new Alias(indexPrefix));

        client.indices().create(request, RequestOptions.DEFAULT);

    }

    @Override
    public void addAlias(String indexName, long tableId) throws IOException {
        IndicesAliasesRequest request=new IndicesAliasesRequest();

        IndicesAliasesRequest.AliasActions aliasActions=new IndicesAliasesRequest.AliasActions(IndicesAliasesRequest.AliasActions.Type.ADD)
                .index(indexName)
                .alias(aliasPrefix+tableId)
                .filter(QueryBuilders.termQuery("table_id",tableId));
        request.addAliasAction(aliasActions);

        client.indices().updateAliases(request,RequestOptions.DEFAULT);
    }

    @Override
    public void privilegetest(String content,String[] disabledtable,int curpage,int pagesize) throws IOException {
        SearchRequest request = new SearchRequest("sytxgspt2");
        BoolQueryBuilder boolQueryBuilder=newquery(content,disabledtable);
        SearchSourceBuilder sourceBuildernews=new SearchSourceBuilder()
                .query(boolQueryBuilder)
                .from((curpage-1)*pagesize)
                .size(pagesize);
        request.source(sourceBuildernews);
        SearchResponse response=client.search(request,RequestOptions.DEFAULT);
        log.info("the response is {}",response);


    }

}
