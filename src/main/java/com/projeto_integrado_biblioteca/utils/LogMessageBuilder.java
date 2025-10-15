package com.projeto_integrado_biblioteca.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;

public final class LogMessageBuilder {
    private LogMessageBuilder() {
    }

    public static String build(HttpStatus status, Exception e, HttpServletRequest request) {
        return String.format("❗[%d - %s] %s - %s",
                status.value(),
                status.getReasonPhrase(),
                e.getMessage(),
                request.getRequestURI()
        );
    }

    public static String build(HttpStatus status, String message, HttpServletRequest request) {
        return String.format("❗[%d - %s] %s - %s",
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getRequestURI()
        );
    }
}
