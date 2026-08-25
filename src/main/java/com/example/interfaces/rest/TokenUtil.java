package com.example.interfaces.rest;

import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public final class TokenUtil {

    private TokenUtil() {
    }

    public static String generateToken(String username, Set<String> groups) throws Exception {
        PrivateKey privateKey = loadPrivateKey();

        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .issuer("https://hantsy.github.io/jakartaee-security-example")
                .subject(username)
                .jwtID(UUID.randomUUID().toString())

                // upn and groups is required by MP-JWT
                .claim("upn", username)
                .claim("groups", List.copyOf(groups))
                .issueTime(Date.from(Instant.now()))
                .expirationTime(Date.from(Instant.now().plusSeconds(3600)))
                .build();

        JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.RS256)
                .type(JOSEObjectType.JWT)
                .build();

        SignedJWT jwt = new SignedJWT(header, claims);
        jwt.sign(new RSASSASigner(privateKey));
        return jwt.serialize();
    }

    private static PrivateKey loadPrivateKey() throws Exception {
        try (var in = TokenUtil.class.getResourceAsStream("/META-INF/keys/privatekey.pem")) {
            if (in == null) {
                throw new IllegalStateException("META-INF/keys/privatekey.pem not found on classpath");
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
