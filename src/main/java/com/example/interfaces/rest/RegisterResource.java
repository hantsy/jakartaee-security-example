package com.example.interfaces.rest;

import com.example.application.RegisterUserUseCase;
import com.example.domain.model.RoleType;
import com.example.domain.model.UserAccount;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.stream.Collectors;

@RequestScoped
@Path("register")
public class RegisterResource {

    @Inject
    private RegisterUserUseCase registerUserUseCase;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(@Valid RegisterRequest request) {
        UserAccount user = registerUserUseCase.register(request.username(), request.email(), request.password());
        return Response.status(Response.Status.CREATED)
                .entity(new RegisterResponse(user.getUsername(), user.getRoles().stream().map(RoleType::getRoleName).collect(Collectors.toSet())))
                .build();
    }
}
