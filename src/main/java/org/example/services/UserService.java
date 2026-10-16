package org.example.services;

import org.example.annotation.Cumponent;
import org.example.factories.UserFactory;
import org.example.models.Article;
import org.example.models.User;
import org.example.repositories.ArticleRepository;
import org.example.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Cumponent
@Service
public class UserService {
    private final UserRepository repository;
    private final ArticleRepository articleRepository;
    private final UserFactory factory;

    public UserService(
            UserRepository repository,
            ArticleRepository articleRepository,
            UserFactory factory) {
        this.repository = repository;
        this.articleRepository = articleRepository;
        this.factory = factory;
    }

    public List<User> findAll() {
        return repository.findAll();
    }

    public User findById(Long id) {
        User user = repository.findById(id);
        if (user == null) {
            throw new NoSuchElementException("Пользователь с ID " + id + " не найден");
        }
        return user;
    }

    public User create(String name) {
        return repository.save(factory.create(name));
    }

    public User save(User user) {
        return repository.save(user);
    }

    public void deleteById(Long id) {
        findById(id);

        boolean hasArticles = articleRepository.findAll().stream()
                .anyMatch(article -> article.getAuthor() != null
                        && Objects.equals(article.getAuthor().getId(), id));

        if (hasArticles) {
            throw new IllegalArgumentException(
                    "Нельзя удалить пользователя: он является автором существующих статей");
        }

        repository.deleteById(id);
    }
}