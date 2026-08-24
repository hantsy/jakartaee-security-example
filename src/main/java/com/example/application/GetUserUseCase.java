package com.example.application;

import com.example.domain.model.UserAccount;
import com.example.domain.repository.UserAccountRepository;
import jakarta.inject.Inject;

import java.util.Optional;

@UseCase
public class GetUserUseCase {

    @Inject
    private UserAccountRepository repository;

    public Optional<UserAccount> getById(Long id) {
        return repository.findById(id);
    }
}
