package com.example.tiexies.service;

import com.example.tiexies.entity.AggDBVO;
import com.example.tiexies.entity.ESIndexDTO;
import com.example.tiexies.entity.Result;

import java.io.IOException;

public interface AggService {

    Result aggByDB(String content, int curpage, int pagesize,String... disabledtable) throws IOException;

    Result typesearchByDB(String indexname,String content,int curpage,int pagesize,String... disabledtable) throws IOException;

    Result getTableName(String content) throws IOException;
}
