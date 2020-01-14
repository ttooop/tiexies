package com.example.tiexies;

import com.example.tiexies.service.AggService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
class TiexiesApplicationTests {

    @Autowired
    private AggService aggService;

    @Test
    void contextLoads() throws IOException {
        aggService.aggByDB("铁西",1,1);
    }

}
