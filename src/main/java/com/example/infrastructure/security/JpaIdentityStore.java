package com.example.infrastructure.security;

import com.example.domain.repository.UserAccountRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.credential.Credential;
import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
import jakarta.security.enterprise.identitystore.IdentityStore;
import jakarta.security.enterprise.identitystore.Pbkdf2PasswordHash;

import java.util.Set;

@ApplicationScoped
public class JpaIdentityStore implements IdentityStore {

    @Inject
    private UserAccountRepository repository;

    @Inject
    private Pbkdf2PasswordHash passwordHash;

    @Override
    public CredentialValidationResult validate(Credential credential) {
        if (!(credential instanceof UsernamePasswordCredential upc)) {
            return CredentialValidationResult.NOT_VALIDATED_RESULT;
        }

        return repository.findByUsername(upc.getCaller())
                .filter(user -> passwordHash.verify(upc.getPassword().getValue(), user.getPassword()))
                .filter(user -> user.isEnabled())
                .map(user -> new CredentialValidationResult(user.getUsername(), Set.of(user.getRole().getRoleName())))
                .orElse(CredentialValidationResult.INVALID_RESULT);
    }
}
