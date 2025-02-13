package com.teamFresh.order.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(OutOfStockException.class)
    public ResponseEntity<ServerExceptionResponse> handleOutOfStockException(OutOfStockException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ServerExceptionResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), ex.getReason())
        );
    }

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ServerExceptionResponse> handleApplicationException(ApplicationException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ServerExceptionResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), ex.getReason())
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ServerExceptionResponse> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ServerExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), ex.getMessage())
        );
    }

    public record ServerExceptionResponse(int code, String message, String reason) {
    }

}
