package com.project.studytogether.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDto<T> {

    private int code;
    private String message;
    private T data;

    public ResponseDto(int code, String message){
        this.code=code;
        this.message=message;
    }
}
