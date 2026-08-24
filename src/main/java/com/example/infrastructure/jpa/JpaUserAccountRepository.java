package com.example.infrastructure.jpa;

import com.example.domain.model.UserAccount;
import com.example.domain.repository.UserAccountRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.util.List;
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
    public Optional<UserAccount> findById(Long id) {
        return Optional.ofNullable(em.find(UserAccount.class, id));
    }

    @Override
    @Transactional
    public List<UserAccount> findAll() {
        return em.createQuery("select u from UserAccount u order by u.createdAt", UserAccount.class)
                .getResultList();
    }

    @Override
    @Transactional
    public UserAccount save(UserAccount user) {
        em.persist(user);
        return user;
    }

    @Override
    @Transactional
    public UserAccount update(UserAccount user) {
        return em.merge(user);
    }

    @Override
    @Transactional
    public void delete(UserAccount user) {
        em.remove(em.merge(user));
    }
}
