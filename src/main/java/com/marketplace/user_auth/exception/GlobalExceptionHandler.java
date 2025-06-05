package com.marketplace.user_auth.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private record  ValidationErrorItem(String key, String message){};

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ex.getBindingResult()
                        .getAllErrors()
                        .stream()
                        .map(
                                (  e) -> {
                                  return new ValidationErrorItem(((FieldError) e).getField(), e.getDefaultMessage());
                                }
                        ).toList()
        );
    }
}