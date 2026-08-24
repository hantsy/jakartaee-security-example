package com.example.application;

import com.example.domain.model.UserAccount;
import com.example.domain.repository.UserAccountRepository;
import jakarta.inject.Inject;

@UseCase
public class ToggleUserEnabledUseCase {

    @Inject
    private UserAccountRepository repository;

    public void toggle(Long id) {
        UserAccount user = repository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        user.setEnabled(!user.isEnabled());
        repository.update(user);
    }
}
