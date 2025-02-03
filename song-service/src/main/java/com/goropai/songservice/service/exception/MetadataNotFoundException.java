package com.goropai.songservice.service.exception;

public class MetadataNotFoundException extends RuntimeException {
    private final String id;

    public MetadataNotFoundException() {
        super();
        id = "";
    }

    public MetadataNotFoundException(int id) {
        this.id = String.valueOf(id);
    }

    @Override
    public String getMessage() {
        return String.format("Resource with ID=%s not found", id);
    }
}
