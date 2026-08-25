package com.example.interfaces.rest;

import com.example.application.UsernameAlreadyExistsException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class UsernameAlreadyExistsExceptionMapper implements ExceptionMapper<UsernameAlreadyExistsException> {

    @Override
    public Response toResponse(UsernameAlreadyExistsException exception) {
        return Response.status(Response.Status.CONFLICT)
                .type(MediaType.APPLICATION_JSON)
                .entity(ProblemDetail.forStatus(Response.Status.CONFLICT, exception.getMessage()))
                .build();
    }
}
