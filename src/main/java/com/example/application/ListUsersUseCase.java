package com.example.application;

import com.example.domain.model.UserAccount;
import com.example.domain.repository.UserAccountRepository;
import jakarta.inject.Inject;

import java.util.List;

@UseCase
public class ListUsersUseCase {

    @Inject
    private UserAccountRepository repository;

    public List<UserAccount> list() {
        return repository.findAll();
    }
}
