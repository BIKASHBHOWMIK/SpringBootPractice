package com.bhowmikbikash.SpringBootPractice.exception;

public class EmployeeAlreadyExistException extends RuntimeException{


    public EmployeeAlreadyExistException(String message) {
        super(message);
    }


    public EmployeeAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }


    public EmployeeAlreadyExistException(Throwable cause) {
        super(cause);
    }
}
