package com.goropai.songservice.service.exception;

public class MetadataAlreadyExistException extends RuntimeException {
    private final int id;

    public MetadataAlreadyExistException(int id) {
        this.id = id;
    }

    @Override
    public String getMessage() {
        return String.format("Metadata for this ID=%d already exists", id);
    }
}
