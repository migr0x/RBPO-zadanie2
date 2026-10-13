package org.example.repositories;

import org.example.models.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserRepository {
    private final Map<Long, User> storage = new HashMap<>();
    private long idCounter = 1;

    public List<User> findAll() {
        return new ArrayList<>(storage.values());
    }

    public User findById(Long id) {
        return storage.get(id);
    }

    public User save(User user) {
        if (user.getId() == null) {
            user.setId(idCounter++);
        }
        storage.put(user.getId(), user);
        return user;
    }

    public void deleteById(Long id) {
        storage.remove(id);
    }
}