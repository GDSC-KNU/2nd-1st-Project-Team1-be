package com.gdsc.canigraduate.dto.response;

import lombok.Data;

/**
 * Created by im2sh
 */

@Data
public class ResponseDto {
    private String message;

    public ResponseDto(String message){
        this.message = message;
    }
}
