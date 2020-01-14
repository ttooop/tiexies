package com.example.tiexies.service;

import com.example.tiexies.entity.ESIndexDTO;

import java.io.IOException;

public interface IndexService {

    void createIndex(ESIndexDTO esIndexDTO) throws IOException;

    void addAlias(String indexName,long tableId) throws IOException;
}
