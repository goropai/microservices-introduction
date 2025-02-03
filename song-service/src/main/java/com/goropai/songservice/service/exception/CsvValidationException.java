package com.goropai.songservice.service.exception;

public class CsvValidationException extends RuntimeException{
    public CsvValidationException(String message) {
        super(message);
    }
}