package com.projeto_integrado_biblioteca.exceptions.controller;

import com.projeto_integrado_biblioteca.exceptions.ConflictException;
import com.projeto_integrado_biblioteca.exceptions.InvalidRequestException;
import com.projeto_integrado_biblioteca.exceptions.ResourceNotFoundException;
import com.projeto_integrado_biblioteca.utils.LogMessageBuilder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;

@Slf4j
@ControllerAdvice
public class AdviceController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdviceController.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAnyException(Exception e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        LOGGER.error("💥 [500] Erro inesperado em {}: {}", request.getRequestURI(), e.getMessage(), e);

        ErrorResponse response = buildResponse(
                e.getMessage(),
                status,
                request.getRequestURI()
        );

        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(
            MethodArgumentNotValidException e,
            HttpServletRequest request
    ) {
        String message = Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage();
        HttpStatus status = HttpStatus.BAD_REQUEST;

        LOGGER.warn(LogMessageBuilder.build(status, message, request));

        ErrorResponse response = buildResponse(
                message,
                status,
                request.getRequestURI()
        );

        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflict(
            ConflictException e,
            HttpServletRequest request
    ) {
        HttpStatus status = HttpStatus.CONFLICT;
        LOGGER.warn(LogMessageBuilder.build(status, e, request));

        ErrorResponse response = buildResponse(
                e.getMessage(),
                status,
                request.getRequestURI()
        );
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(
            BadCredentialsException e,
            HttpServletRequest request
    ) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        LOGGER.warn(LogMessageBuilder.build(status, e, request));

        ErrorResponse response = buildResponse(
                e.getMessage(),
                status,
                request.getRequestURI()
        );
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(
            ResourceNotFoundException e,
            HttpServletRequest request
    ) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        LOGGER.warn(LogMessageBuilder.build(status, e, request));

        ErrorResponse response = buildResponse(
                e.getMessage(),
                status,
                request.getRequestURI()
        );
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<ErrorResponse> handleInvalidRequest(
            ResourceNotFoundException e,
            HttpServletRequest request
    ) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        LOGGER.warn(LogMessageBuilder.build(status, e, request));

        ErrorResponse response = buildResponse(
                e.getMessage(),
                status,
                request.getRequestURI()
        );
        return ResponseEntity.status(status).body(response);
    }

    private ErrorResponse buildResponse(String message, HttpStatus status, String path) {
        Instant timestamp = ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")).toInstant();

        return new ErrorResponse(
                timestamp,
                status.value(),
                status.getReasonPhrase(),
                message,
                path
        );
    }
}
