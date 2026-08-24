package com.example.interfaces.rest;

import java.util.Set;

public record RegisterResponse(String username, Set<String> roles) {
}
