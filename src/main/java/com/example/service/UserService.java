package com.example.service;

import com.example.model.User;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class UserService implements IUserService {
    @Inject
    EntityManager em;

    public Optional<User> findOne(UUID id) {
        User user = em.find(User.class, id);
        return Optional.ofNullable(user);
    }

    public List<User> findAll() {
        TypedQuery<User> query = em.createQuery("select user from User user", User.class);
        return query.getResultList();
    }

    @Transactional
    public Optional<User> save(User user) {
        if (user == null) {
            return Optional.empty();
        }
        if (!this.exist(user.getId())) {
            em.persist(user);
            em.flush();
            return Optional.of(user);
        }
        user = em.merge(user);
        em.flush();
        return Optional.of(user);
    }

    @Transactional
    public void delete(UUID id) {
        User user = em.find(User.class, id);
        if (user != null) {
            em.detach(user);
            em.flush();
        }
    }

    public boolean exist(UUID id) {
        if (id == null) {
            return false;
        }
        return em.find(User.class, id) != null;
    }
}
