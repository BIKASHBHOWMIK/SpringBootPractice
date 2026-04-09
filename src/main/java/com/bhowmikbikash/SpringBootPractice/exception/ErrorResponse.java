package com.bhowmikbikash.SpringBootPractice.exception;

import lombok.Data;

@Data
public class ErrorResponse {

    private Integer statusCode;
    private String errorMessage;

    public ErrorResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
