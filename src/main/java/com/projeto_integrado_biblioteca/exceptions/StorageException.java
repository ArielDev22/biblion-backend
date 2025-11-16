package com.projeto_integrado_biblioteca.exceptions;

import org.springframework.http.HttpStatus;

public class StorageException extends RuntimeException {
    public static final HttpStatus HTTP_STATUS = HttpStatus.INTERNAL_SERVER_ERROR;

    public StorageException(String message) {
        super(message);
    }
}
