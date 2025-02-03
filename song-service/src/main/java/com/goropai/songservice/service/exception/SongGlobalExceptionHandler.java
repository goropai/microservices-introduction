package com.goropai.songservice.service.exception;

import com.goropai.songservice.entity.dto.SimpleErrorResponse;
import com.goropai.songservice.entity.dto.ValidationErrorResponse;
import jakarta.persistence.PersistenceException;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class SongGlobalExceptionHandler {

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handleAll(Exception ex) {
        Throwable rootCause = findRootCause(ex);
        HttpStatus status = (rootCause instanceof ConstraintViolationException) ? HttpStatus.BAD_REQUEST
                : HttpStatus.INTERNAL_SERVER_ERROR;
        return ResponseEntity.status(status).body(new SimpleErrorResponse(ex.getMessage(), status.value()));
    }

    private Throwable findRootCause(Throwable ex) {
        Throwable cause = ex;
        while (cause.getCause() != null) {
            cause = cause.getCause();
        }
        return cause;
    }

    @ExceptionHandler(value = {PersistenceException.class})
    public ResponseEntity<Object> handle(PersistenceException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new SimpleErrorResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }

    @ExceptionHandler(value = {MetadataNotFoundException.class})
    public ResponseEntity<Object> handle(MetadataNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON)
                .body(new SimpleErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value()));
    }

    @ExceptionHandler(value = {MetadataAlreadyExistException.class})
    public ResponseEntity<Object> handle(MetadataAlreadyExistException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).contentType(MediaType.APPLICATION_JSON)
                .body(new SimpleErrorResponse(ex.getMessage(), HttpStatus.CONFLICT.value()));
    }

    @ExceptionHandler(value = {ValidationException.class})
    public ResponseEntity<Object> handle(ValidationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON)
                .body(new ValidationErrorResponse("Validation error", getValidations(ex.getMessage()), "400"));
    }

    @ExceptionHandler(value = {CsvValidationException.class})
    public ResponseEntity<Object> handle(CsvValidationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON)
                .body(new SimpleErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST.value()));
    }

    private Map<String, String> getValidations(String message) {
        return Arrays.stream(message.split(",")).map(s -> {
            String[] parts = s.split(":");
            return new AbstractMap.SimpleEntry<>(parts[0], parts[1]);
        }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue,
                LinkedHashMap::new));
    }
}
