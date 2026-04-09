package com.bhowmikbikash.SpringBootPractice.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<ErrorResponse> HandleEmployeeNotFound(EmployeeNotFoundException ex) {
        log.info("Employee not found : {} ", ex.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
        errorResponse.setStatusCode(HttpStatus.NOT_FOUND.value());

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(EmployeeAlreadyExistException.class)
    public ResponseEntity<ErrorResponse> handleEmployeeAlreadyExist(EmployeeAlreadyExistException ex) {
        log.info("Employee already Exists : {} ", ex.getMessage());

        ErrorResponse body = new ErrorResponse(ex.getMessage());
        body.setStatusCode(HttpStatus.CONFLICT.value());

        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex) {
        log.info("Exception : {} ", ex.getMessage());

        ErrorResponse body = new ErrorResponse(ex.getMessage());
        body.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());

        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
