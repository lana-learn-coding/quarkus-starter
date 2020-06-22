package com.example;

import com.example.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

// TODO: use h2 + hibernate + jdbc for embedded data source
public class UserEmbeddedDB {
    private static final UserEmbeddedDB INSTANCE = new UserEmbeddedDB();

    private UserEmbeddedDB() {
    }

    public static UserEmbeddedDB getInstance() {
        return INSTANCE;
    }

    private final ArrayList<User> usersDb = new ArrayList<>();

    public Optional<User> findOne(String id) {
        for (User user : usersDb) {
            if (user.getId().equals(id)) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    public List<User> findAllUser() {
        return new ArrayList<>(usersDb);
    }

    public Optional<User> save(User user) {
        if (user == null) {
            return Optional.empty();
        }
        if (user.getId() != null) {
            usersDb.removeIf(existed -> existed.getId().equals(user.getId()));
        } else {
            user.setId(UUID.randomUUID().toString());
        }

        return usersDb.add(user) ? Optional.of(user) : Optional.empty();
    }

    public void delete(String id) {
        usersDb.removeIf(user -> user.getId().equals(id));
    }

    public boolean isExist(String id) {
        return usersDb
            .stream()
            .anyMatch(user -> user.getId().equals(id));
    }
}
