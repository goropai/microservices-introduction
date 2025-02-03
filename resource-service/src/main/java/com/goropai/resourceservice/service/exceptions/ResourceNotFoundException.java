package com.goropai.resourceservice.service.exceptions;

public class ResourceNotFoundException extends RuntimeException{
    private final String id;

    public ResourceNotFoundException() {
        super();
        id = "";
    }

    public ResourceNotFoundException(int id) {
        this.id = String.valueOf(id);
    }

    @Override
    public String getMessage() {
        return String.format("Resource with ID=%s not found", id);
    }
}
