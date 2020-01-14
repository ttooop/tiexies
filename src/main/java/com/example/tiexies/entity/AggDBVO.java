package com.example.tiexies.entity;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class AggDBVO {
    //命中的文档数
    private long docnum=0L;

    private List<Map<String,Long>> numofeachdb;

    private List<Map<String,Object>> agginfo;
}
