package com.example.tiexies.entity;

import lombok.Data;

@Data
public class Result {
    //成功success失败fail
    private String status;

    private Object data;

    public static Result create(Object result){
        return Result.create(result,"success");
    }

    public static Result create(Object result,String status){
        Result type=new Result();
        type.setData(result);
        type.setStatus(status);
        return type;
    }
}
