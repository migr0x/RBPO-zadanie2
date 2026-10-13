package org.example.repositories;

import org.example.models.Article;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ArticleRepository {
    private final Map<Long, Article> storage = new HashMap<>();
    private long idCounter = 1;

    public List<Article> findAll() { return new ArrayList<>(storage.values()); }
    public Article findById(Long id) { return storage.get(id); }
    public Article save(Article article) {
        if (article.getId() == null) article.setId(idCounter++);
        storage.put(article.getId(), article);
        return article;
    }
    public void deleteById(Long id) { storage.remove(id); }
}