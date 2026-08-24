package com.example.interfaces.rest;

import java.util.Set;

public record MeResponse(String username, Set<String> roles) {
}
