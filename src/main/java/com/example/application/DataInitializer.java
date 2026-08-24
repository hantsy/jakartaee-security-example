package com.example.application;

import com.example.domain.model.RoleType;
import com.example.domain.model.UserAccount;
import com.example.domain.repository.UserAccountRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.event.Startup;
import jakarta.inject.Inject;
import jakarta.security.enterprise.identitystore.Pbkdf2PasswordHash;

import java.util.Set;
import java.util.logging.Logger;

@ApplicationScoped
public class DataInitializer {

    private static final Logger LOG = Logger.getLogger(DataInitializer.class.getName());

    @Inject
    private UserAccountRepository repository;

    @Inject
    private Pbkdf2PasswordHash passwordHash;

    public void onStartup(@Observes Startup event) {
        repository.save(new UserAccount("admin", "admin@example.com", passwordHash.generate("password".toCharArray()), Set.of(RoleType.ADMIN)));
        repository.save(new UserAccount("user", "user@example.com", passwordHash.generate("password".toCharArray()), Set.of(RoleType.USER)));
        LOG.info("Seeded identity store users: admin, user");
    }
}
