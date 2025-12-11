// com/example/planning/exceptions/RnotFound.java

package com.example.planning_service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND) // Pour retourner HTTP 404
public class RnotFound extends RuntimeException {

    public RnotFound(String message) {
        super(message);
    }
}