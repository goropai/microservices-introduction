package com.goropai.resourceservice.service.exceptions;

public class CsvValidationException extends RuntimeException{
    public CsvValidationException(String message) {
        super(message);
    }
}
