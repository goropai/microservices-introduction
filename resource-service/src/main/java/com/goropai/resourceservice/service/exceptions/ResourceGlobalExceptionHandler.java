package com.goropai.resourceservice.service.exceptions;

import com.goropai.resourceservice.entity.dto.SimpleErrorResponse;
import com.goropai.resourceservice.entity.dto.ValidationErrorResponse;
import jakarta.persistence.PersistenceException;
import jakarta.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.reactive.function.client.WebClientException;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ResourceGlobalExceptionHandler {

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handleAll(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new SimpleErrorResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }

    @ExceptionHandler(value = {PersistenceException.class})
    public ResponseEntity<Object> handle(PersistenceException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new SimpleErrorResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }

    @ExceptionHandler(value = {ResourceNotFoundException.class})
    public ResponseEntity<Object> handle(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON)
                .body(new SimpleErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value()));
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

    @ExceptionHandler(value = {MethodArgumentTypeMismatchException.class})
    public ResponseEntity<Object> handle(MethodArgumentTypeMismatchException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON)
                .body(new SimpleErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST.value()));
    }

    @ExceptionHandler(value = {HandlerMethodValidationException.class})
    public ResponseEntity<Object> handle(HandlerMethodValidationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON)
                .body(new SimpleErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST.value()));
    }

    private Map<String, String> getValidations(String message) {
        if (message.contains("{") && message.contains("}")) {
            message = message.substring(message.lastIndexOf('{') + 1, message.indexOf('}')).replace("\"", "");
        }
        return Arrays.stream(message.split(",")).map(s -> {
            String[] parts = s.split(":");
            return new AbstractMap.SimpleEntry<>(parts[0], parts[1]);
        }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue,
                LinkedHashMap::new));
    }

    @ExceptionHandler(value = {WebClientException.class})
    public ResponseEntity<Object> handle(WebClientException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON)
                .body(new SimpleErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST.value()));
    }
}