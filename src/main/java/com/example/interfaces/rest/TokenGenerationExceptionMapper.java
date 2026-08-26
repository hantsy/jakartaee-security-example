package com.example.interfaces.rest;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class TokenGenerationExceptionMapper implements ExceptionMapper<TokenGenerationException> {

    @Override
    public Response toResponse(TokenGenerationException exception) {
        return Response.status(Response.Status.BAD_REQUEST)
                .type(MediaType.APPLICATION_JSON)
                .entity(ProblemDetail.forStatus(Response.Status.BAD_REQUEST, exception.getMessage()))
                .build();
    }
}
