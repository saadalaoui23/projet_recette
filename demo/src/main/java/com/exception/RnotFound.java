package com.exception;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND) // Indique à Spring de retourner HTTP 404
public class RnotFound extends RuntimeException {

    public RnotFound(String message) {
        super(message);
    }
}