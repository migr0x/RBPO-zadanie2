package ru.mtusi.rbpo.lab2.service;

import org.springframework.stereotype.Service;
import ru.mtusi.rbpo.lab2.dto.UserRequest;
import ru.mtusi.rbpo.lab2.exception.ResourceConflictException;
import ru.mtusi.rbpo.lab2.exception.ResourceNotFoundException;
import ru.mtusi.rbpo.lab2.model.User;
import ru.mtusi.rbpo.lab2.repository.ArticleRepository;
import ru.mtusi.rbpo.lab2.repository.UserRepository;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private final ArticleService articleService;

    public UserService(UserRepository userRepository,
                       ArticleRepository articleRepository,
                       ArticleService articleService) {
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
        this.articleService = articleService;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
    }

    public User create(UserRequest request) {
        requireText(request.username(), "username");
        requireText(request.displayName(), "displayName");
        ensureUnique(request.username(), request.email(), null);
        return userRepository.save(new User(null, request.username().trim(),
                request.displayName().trim(), normalizeEmail(request.email())));
    }

    public User update(Long id, UserRequest request) {
        User current = findById(id);
        String username = request.username() == null ? current.username() : request.username().trim();
        String displayName = request.displayName() == null ? current.displayName() : request.displayName().trim();
        String email = request.email() == null ? current.email() : normalizeEmail(request.email());
        if (request.username() != null) requireText(request.username(), "username");
        if (request.displayName() != null) requireText(request.displayName(), "displayName");
        ensureUnique(username, email, id);
        return userRepository.save(new User(id, username, displayName, email));
    }

    public void deleteById(Long id) {
        findById(id);
        articleRepository.findByAuthorId(id).stream()
                .map(article -> article.id())
                .toList()
                .forEach(articleService::deleteById);
        userRepository.deleteById(id);
    }

    private void ensureUnique(String username, String email, Long currentId) {
        boolean usernameTaken = userRepository.findAll().stream()
                .anyMatch(user -> !user.id().equals(currentId)
                        && user.username().equalsIgnoreCase(username));
        if (usernameTaken) throw new ResourceConflictException("username is already in use");
        if (email != null) {
            boolean emailTaken = userRepository.findAll().stream()
                    .anyMatch(user -> !user.id().equals(currentId) && user.email() != null
                            && user.email().equalsIgnoreCase(email));
            if (emailTaken) throw new ResourceConflictException("email is already in use");
        }
    }

    private String normalizeEmail(String email) {
        if (email == null || email.isBlank()) return null;
        String normalized = email.trim();
        if (!normalized.contains("@")) throw new IllegalArgumentException("email must contain @");
        return normalized;
    }

    private void requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " must not be blank");
        }
    }
}
