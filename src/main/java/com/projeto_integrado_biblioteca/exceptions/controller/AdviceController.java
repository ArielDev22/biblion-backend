package com.projeto_integrado_biblioteca.exceptions.controller;

import com.projeto_integrado_biblioteca.exceptions.EmailAlreadyUsedException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

@ControllerAdvice
public class AdviceController {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAnyException(Exception e, HttpServletRequest request) {
        String message = e.getMessage();
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String path = request.getRequestURI();

        return ResponseEntity.status(status).body(buildResponse(message, status, path));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(
            MethodArgumentNotValidException e,
            HttpServletRequest request
    ) {
        String message = Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage();
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String path = request.getRequestURI();

        return ResponseEntity.status(status).body(buildResponse(message, status, path));
    }

    @ExceptionHandler(EmailAlreadyUsedException.class)
    public ResponseEntity<ErrorResponse> handleEmailAlreadyUsed(
            EmailAlreadyUsedException e,
            HttpServletRequest request
    ) {
        String message = e.getMessage();
        HttpStatus status = HttpStatus.CONFLICT;
        String path = request.getRequestURI();

        return ResponseEntity.status(status).body(buildResponse(message, status, path));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(
            BadCredentialsException e,
            HttpServletRequest request
    ) {
        String message = e.getMessage();
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String path = request.getRequestURI();

        return ResponseEntity.status(status).body(buildResponse(message, status, path));
    }

    private ErrorResponse buildResponse(String message, HttpStatus status, String path) {
        Instant timestamp = LocalDateTime.now().toInstant(ZoneOffset.of("-03:00"));

        return new ErrorResponse(message, status, timestamp, path);
    }
}
