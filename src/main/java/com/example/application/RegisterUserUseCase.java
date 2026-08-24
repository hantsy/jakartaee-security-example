package com.example.application;

import com.example.domain.event.UserRegisteredEvent;
import com.example.domain.model.RoleType;
import com.example.domain.model.UserAccount;
import com.example.domain.repository.UserAccountRepository;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.security.enterprise.identitystore.Pbkdf2PasswordHash;

import java.time.Instant;
import java.util.Set;

@UseCase
public class RegisterUserUseCase {

    @Inject
    private UserAccountRepository repository;

    @Inject
    private Pbkdf2PasswordHash passwordHash;

    @Inject
    private Event<UserRegisteredEvent> userRegisteredEvent;

    public UserAccount register(String username, String email, String password) {
        if (repository.findByUsername(username).isPresent()) {
            throw new UsernameAlreadyExistsException(username);
        }

        UserAccount user = repository.save(new UserAccount(username, email, passwordHash.generate(password.toCharArray()), Set.of(RoleType.USER)));

        userRegisteredEvent.fire(new UserRegisteredEvent(user.getUsername(), Instant.now()));

        return user;
    }
}
