package org.example.services;

import org.example.models.User;
import org.example.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public List<User> findAll() { return repository.findAll(); }
    public User findById(Long id) { return repository.findById(id); }
    public User save(User user) { return repository.save(user); }
    public void deleteById(Long id) { repository.deleteById(id); }
}