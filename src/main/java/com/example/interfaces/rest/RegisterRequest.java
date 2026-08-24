package com.example.interfaces.rest;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank @Size(min = 3, max = 50) String username,
        @NotBlank @Email String email,
        @NotBlank @Size(min = 6, max = 64) String password,
        @NotBlank @Size(min = 6, max = 64) String repeatPassword
) {
    public RegisterRequest {
        if (username != null && !username.matches("^[a-zA-Z0-9._-]+$")) {
            throw new IllegalArgumentException("Username may only contain letters, digits, dot, underscore and hyphen");
        }
        if (password != null && !password.matches(".*\\d.*")) {
            throw new IllegalArgumentException("Password must contain at least one digit");
        }
        if (password != null && !password.equals(repeatPassword)) {
            throw new IllegalArgumentException("Password and repeat password do not match");
        }
    }
}
