package com.example.interfaces.rest;

import jakarta.ws.rs.core.Response;

import java.net.URI;

public record ProblemDetail(URI type, String title, int status, String detail, URI instance) {

    public static ProblemDetail forStatus(Response.Status status, String detail) {
        return new ProblemDetail(null, status.getReasonPhrase(), status.getStatusCode(), detail, null);
    }
}
