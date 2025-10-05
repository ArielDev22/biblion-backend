package com.projeto_integrado_biblioteca.exceptions.controller;

import org.springframework.http.HttpStatus;

import java.time.Instant;

public record ErrorResponse(
        String message,
        HttpStatus status,
        Instant timestamp,
        String path
) {
}
