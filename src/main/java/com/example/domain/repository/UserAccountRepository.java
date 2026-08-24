package com.example.domain.repository;

import com.example.domain.model.UserAccount;

import java.util.List;
import java.util.Optional;

public interface UserAccountRepository {

    Optional<UserAccount> findByUsername(String username);

    Optional<UserAccount> findById(Long id);

    List<UserAccount> findAll();

    UserAccount save(UserAccount user);

    UserAccount update(UserAccount user);

    void delete(UserAccount user);
}
