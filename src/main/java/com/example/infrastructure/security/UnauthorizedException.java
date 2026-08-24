package com.example.infrastructure.security;

import jakarta.security.enterprise.AuthenticationException;

public class UnauthorizedException extends AuthenticationException {
    public UnauthorizedException() {
        super("401 Unauthorized");
    }

    public UnauthorizedException(String message) {
        super(message);
    }
}
