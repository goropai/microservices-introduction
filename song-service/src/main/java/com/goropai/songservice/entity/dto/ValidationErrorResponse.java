package com.goropai.songservice.entity.dto;

import java.util.Map;

public class ValidationErrorResponse {
    private String errorMessage;
    private Map<String, String> details;
    private String errorCode;

    public ValidationErrorResponse() {
    }

    public ValidationErrorResponse(String errorMessage, Map<String, String> details, String errorCode) {
        this.errorMessage = errorMessage;
        this.details = details;
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public Map<String, String> getDetails() {
        return details;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
