package com.example.infrastructure.jpa;

import com.example.domain.model.UserAccount;
import com.example.domain.repository.UserAccountRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.util.Optional;

@ApplicationScoped
public class JpaUserAccountRepository implements UserAccountRepository {

    @Inject
    private EntityManager em;

    @Override
    @Transactional
    public Optional<UserAccount> findByUsername(String username) {
        return em.createQuery("select u from UserAccount u where u.username = :username", UserAccount.class)
                .setParameter("username", username)
                .getResultStream()
                .findFirst();
    }

    @Override
    @Transactional
    public UserAccount save(UserAccount user) {
        em.persist(user);
        return user;
    }
}
