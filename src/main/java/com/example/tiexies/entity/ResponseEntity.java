package com.example.tiexies.entity;

import lombok.Data;
import org.springframework.http.HttpStatus;
@Data
public class ResponseEntity<T> {
    private int status;
    private Object message;
    private T body;

    public static ResponseEntity ok(){
        ResponseEntity responseEntity=new ResponseEntity();
        responseEntity.message= HttpStatus.OK.getReasonPhrase();
        responseEntity.status=HttpStatus.OK.value();
        return responseEntity;
    }

    public static <T> ResponseEntity<T> ok(T body){
        ResponseEntity<T> responseEntity=new ResponseEntity<>();
        responseEntity.message=HttpStatus.OK.getReasonPhrase();
        responseEntity.status=HttpStatus.OK.value();
        responseEntity.body=body;
        return responseEntity;
    }

    public static <T> ResponseEntity<T> error(HttpStatus httpcode,Object message){
        ResponseEntity<T> responseEntity=new ResponseEntity<>();
        responseEntity.message=message;
        responseEntity.status=httpcode.value();
        return responseEntity;
    }
}
