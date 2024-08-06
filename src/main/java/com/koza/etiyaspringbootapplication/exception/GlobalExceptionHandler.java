package com.koza.etiyaspringbootapplication.exception;

import com.koza.etiyaspringbootapplication.entity.Role;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({GenericException.class})
    public ResponseEntity<Object> handleGenericException(GenericException exception){
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(exception.getErrorMessage());
    }
    @ExceptionHandler({ModelNotFoundException.class})
    public ResponseEntity<Object> handleModelNotFoundException(ModelNotFoundException exception){
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(exception.getMessage());
    }
}
