package com.example.infrastructure.security;

import jakarta.security.enterprise.AuthenticationException;

public class ForbiddenException extends AuthenticationException {
    public ForbiddenException() {
        super("403 Forbidden");
    }
}
