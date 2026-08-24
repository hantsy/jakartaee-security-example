package com.example.interfaces.rest;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.credential.Password;
import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
import jakarta.security.enterprise.identitystore.IdentityStore;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Set;

@RequestScoped
@Path("token")
public class TokenResource {

    @Inject
    private IdentityStore identityStore;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response generateToken(TokenRequest request) {
        CredentialValidationResult result = identityStore.validate(
                new UsernamePasswordCredential(request.username(), new Password(request.password())));

        if (result.getStatus() != CredentialValidationResult.Status.VALID) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        try {
            return Response.ok(new TokenResponse(issueToken(request.username(), result.getCallerGroups()))).build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    private String issueToken(String username, Set<String> groups) throws Exception {
        PrivateKey privateKey = loadPrivateKey();

        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .issuer("jakartaee12-sandbox")
                .subject(username)
                .claim("groups", List.copyOf(groups))
                .issueTime(Date.from(Instant.now()))
                .expirationTime(Date.from(Instant.now().plusSeconds(3600)))
                .build();

        SignedJWT jwt = new SignedJWT(new JWSHeader(JWSAlgorithm.RS256), claims);
        jwt.sign(new RSASSASigner(privateKey));
        return jwt.serialize();
    }

    private PrivateKey loadPrivateKey() throws Exception {
        try (var in = getClass().getResourceAsStream("/privatekey.pem")) {
            if (in == null) {
                throw new IllegalStateException("privatekey.pem not found on classpath");
            }
            String pem = new String(in.readAllBytes());
            String base64 = pem
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s", "");
            return KeyFactory.getInstance("RSA")
                    .generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(base64)));
        }
    }
}
