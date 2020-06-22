package com.example.service;

import com.example.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    Optional<User> findOne(UUID id);

    Optional<User> save(User user);

    void delete(UUID id);

    boolean exist(UUID id);

    List<User> findAll();
}
