package com.example.tiexies;

import com.example.tiexies.entity.Result;
import com.example.tiexies.service.AggService;
import com.example.tiexies.service.IndexService;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
class TiexiesApplicationTests {

    @Autowired
    private AggService aggService;
    @Autowired
    private IndexService indexService;

    @Test
    void contextLoads() throws IOException {
        aggService.aggByDB("铁西",1,1);
        aggService.typesearchByDB("cwsw","铁西",1,1);
    }

    @Test
    void privilegetest() throws IOException {
//        indexService.privilegetest("铁西",1,5,"cms_article");
        Result result=aggService.getTableName("铁西");
        System.out.println(result.getData());
    }

}
