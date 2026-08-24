package com.example.application;

import com.example.domain.model.UserAccount;
import com.example.domain.repository.UserAccountRepository;
import jakarta.inject.Inject;

@UseCase
public class DeleteUserUseCase {

    @Inject
    private UserAccountRepository repository;

    public void delete(Long id) {
        UserAccount user = repository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        repository.delete(user);
    }
}
