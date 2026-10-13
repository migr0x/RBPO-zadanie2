package org.example.services;

import org.example.models.Article;
import org.example.repositories.ArticleRepository;
import org.example.repositories.EditorialReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final EditorialReviewRepository reviewRepository;

    public ArticleService(ArticleRepository articleRepository, EditorialReviewRepository reviewRepository) {
        this.articleRepository = articleRepository;
        this.reviewRepository = reviewRepository;
    }

    public List<Article> findAll() { return articleRepository.findAll(); }
    public Article findById(Long id) { return articleRepository.findById(id); }
    public Article save(Article article) { return articleRepository.save(article); }

    public void deleteById(Long id) {
        // Требование: Удаление связанных объектов не должно оставлять недействительных ссылок
        reviewRepository.findAll().stream()
                .filter(review -> review.getArticle() != null && review.getArticle().getId().equals(id))
                .forEach(review -> reviewRepository.deleteById(review.getId()));

        articleRepository.deleteById(id);
    }
}