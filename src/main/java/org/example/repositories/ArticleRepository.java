package org.example.repositories;

import org.example.annotation.Cumponent;
import org.example.models.Article;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

@Cumponent
@Repository
public class ArticleRepository {
    private final ConcurrentMap<Long, Article> storage = new ConcurrentHashMap<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    public List<Article> findAll() {
        return new ArrayList<>(storage.values());
    }

    public Article findById(Long id) {
        return storage.get(id);
    }

    public Article save(Article article) {
        if (article.getId() == null) {
            article.setId(idCounter.getAndIncrement());
        }
        storage.put(article.getId(), article);
        return article;
    }

    public void deleteById(Long id) {
        storage.remove(id);
    }
}