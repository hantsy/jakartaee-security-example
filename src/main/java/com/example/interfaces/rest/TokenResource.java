package com.example.interfaces.rest;

import com.example.infrastructure.security.UnauthorizedException;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.credential.Password;
import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
import jakarta.security.enterprise.identitystore.IdentityStoreHandler;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.logging.Level;
import java.util.logging.Logger;

@RequestScoped
@Path("token")
public class TokenResource {

    private static final Logger LOG = Logger.getLogger(TokenResource.class.getName());

    // Delegates to all IdentityStore beans in priority order.
    @Inject
    private IdentityStoreHandler identityStoreHandler;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response generateToken(TokenRequest request) throws UnauthorizedException {
        CredentialValidationResult result = identityStoreHandler.validate(
                new UsernamePasswordCredential(request.username(), new Password(request.password())));

        if (result.getStatus() != CredentialValidationResult.Status.VALID) {
            throw new UnauthorizedException();
        }

        try {
            return Response.ok(new TokenResponse(TokenUtil.generateToken(request.username(), result.getCallerGroups()))).build();
        } catch (Exception e) {
            LOG.log(Level.WARNING, "Failed to generate token: {0}", e);
            throw new UnauthorizedException();
        }
    }
}
