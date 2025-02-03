package com.goropai.songservice.entity.dto;

public class SimpleErrorResponse {
    private String errorMessage;
    private String errorCode;

    public SimpleErrorResponse() {
    }

    public SimpleErrorResponse(String errorMessage, int errorCode) {
        this.errorMessage = errorMessage;
        this.errorCode = String.valueOf(errorCode);
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
