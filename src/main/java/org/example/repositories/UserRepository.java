package org.example.repositories;

import org.example.annotation.Cumponent;
import org.example.models.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

@Cumponent
@Repository
public class UserRepository {
    private final ConcurrentMap<Long, User> storage = new ConcurrentHashMap<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    public List<User> findAll() {
        return new ArrayList<>(storage.values());
    }

    public User findById(Long id) {
        return storage.get(id);
    }

    public User save(User user) {
        if (user.getId() == null) {
            user.setId(idCounter.getAndIncrement());
        }
        storage.put(user.getId(), user);
        return user;
    }

    public void deleteById(Long id) {
        storage.remove(id);
    }
}