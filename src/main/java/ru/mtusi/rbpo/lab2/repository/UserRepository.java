package ru.mtusi.rbpo.lab2.repository;

import org.springframework.stereotype.Repository;
import ru.mtusi.rbpo.lab2.model.User;

@Repository
public class UserRepository extends AbstractInMemoryRepository<User> {
    public UserRepository() {
        super(User::id, (user, id) -> new User(id, user.username(), user.displayName(), user.email()));
    }

    public boolean existsByUsername(String username) {
        return findAll().stream().anyMatch(user -> user.username().equalsIgnoreCase(username));
    }

    public boolean existsByEmail(String email) {
        return email != null && findAll().stream()
                .anyMatch(user -> user.email() != null && user.email().equalsIgnoreCase(email));
    }
}
