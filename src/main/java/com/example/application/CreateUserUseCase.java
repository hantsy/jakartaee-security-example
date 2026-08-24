package com.example.application;

import com.example.domain.event.UserCreatedEvent;
import com.example.domain.model.RoleType;
import com.example.domain.model.UserAccount;
import com.example.domain.repository.UserAccountRepository;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.security.enterprise.identitystore.Pbkdf2PasswordHash;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Set;

@UseCase
public class CreateUserUseCase {

    private static final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    @Inject
    private UserAccountRepository repository;

    @Inject
    private Pbkdf2PasswordHash passwordHash;

    @Inject
    private Event<UserCreatedEvent> userCreatedEvent;

    public String create(String username, String email, Set<RoleType> roles) {
        String password = generatePassword();
        repository.save(new UserAccount(username, email, passwordHash.generate(password.toCharArray()), roles));
        userCreatedEvent.fire(new UserCreatedEvent(username, Instant.now()));
        return password;
    }

    private String generatePassword() {
        StringBuilder sb = new StringBuilder(12);
        for (int i = 0; i < 12; i++) {
            sb.append(CHARS.charAt(RANDOM.nextInt(CHARS.length())));
        }
        return sb.toString();
    }
}
