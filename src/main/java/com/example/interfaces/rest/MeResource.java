package com.example.interfaces.rest;

import com.example.infrastructure.security.Authenticated;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.SecurityContext;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@RequestScoped
@Path("me")
@Authenticated
public class MeResource {

    @Inject
    private SecurityContext securityContext;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public MeResponse me() {
        return new MeResponse(securityContext.getCallerPrincipal().getName(), securityContext.getAllDeclaredCallerRoles());
    }
}
