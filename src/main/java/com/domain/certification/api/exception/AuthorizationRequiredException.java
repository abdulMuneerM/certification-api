package com.domain.certification.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.PRECONDITION_REQUIRED)
public class AuthorizationRequiredException extends RuntimeException {

    public AuthorizationRequiredException(String message) {
        super(message);
    }
}
