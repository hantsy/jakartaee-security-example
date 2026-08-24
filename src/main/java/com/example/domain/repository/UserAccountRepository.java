package com.example.domain.repository;

import com.example.domain.model.UserAccount;

import java.util.Optional;

public interface UserAccountRepository {

    Optional<UserAccount> findByUsername(String username);

    UserAccount save(UserAccount user);
}
