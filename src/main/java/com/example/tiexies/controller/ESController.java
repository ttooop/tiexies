package com.example.tiexies.controller;

import com.example.tiexies.entity.AggDBVO;
import com.example.tiexies.entity.ResponseEntity;
import com.example.tiexies.entity.Result;
import com.example.tiexies.service.AggService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.print.attribute.standard.MediaSize;
import javax.validation.constraints.Pattern;
import java.io.IOException;

@RestController
@Api(value = "铁西项目大搜索")
@RequestMapping("tiexi")
public class ESController {

    @Autowired
    private AggService aggService;

    @ApiOperation(value = "按数据大类型聚合搜索--首页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "content",value = "搜索词",paramType = "query",required = true),
            @ApiImplicitParam(name = "curpage",value = "当前页",paramType = "query",required = true),
            @ApiImplicitParam(name = "pagesize",value = "页条数",paramType = "query",required = true)
    })
    @GetMapping("/search")
    public Result aggsearchByDB(
            @RequestParam(value = "content",required = true) String content,
            @RequestParam(value = "curpage",required = true) int curpage,
            @RequestParam(value = "pagesize",required = true) int pagesize
    ) throws IOException {
        if (content==" "||content==null){
            return Result.create("抱歉搜索条件不能为空","501");
        }
        return aggService.aggByDB(content,curpage,pagesize);
    }

    @ApiOperation(value = "按点击不同类型进行分类结果显示")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "indexname",value = "索引名",paramType = "query",required = true),
            @ApiImplicitParam(name = "content",value = "搜索词",paramType = "query",required = true),
            @ApiImplicitParam(name = "curpage",value = "当前页",paramType = "query",required = true),
            @ApiImplicitParam(name = "pagesize",value = "页条数",paramType = "query",required = true)
    })
    @GetMapping("/typesearch")
    public Result searchBydata(
            @RequestParam(value = "indexname",required = true) String indexname,
            @RequestParam(value = "content",required = true) String content,
            @RequestParam(value = "curpage",required = true) int curpage,
            @RequestParam(value = "pagesize",required = true) int pagesize
    ) throws IOException {
        if (content==" "||content==null){
            return Result.create("抱歉搜索条件不能为空","501");
        }
        return aggService.typesearchByDB(indexname,content,curpage,pagesize);
    }

}
