package com.grok.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        ErrorMessage errorMsg = new ErrorMessage();
        errorMsg.setDetail(ex.getMessage());
        errorMsg.setError("Record Not Found.");
        errorMsg.setStatus(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(errorMsg, HttpStatus.NOT_FOUND);
    }
}
