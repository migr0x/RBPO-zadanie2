package org.example.services;

import org.example.annotation.Cumponent;
import org.example.factories.ArticleFactory;
import org.example.models.Article;
import org.example.models.EditionIssue;
import org.example.repositories.ArticleRepository;
import org.example.repositories.EditionIssueRepository;
import org.example.repositories.EditorialReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Cumponent
@Service
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final EditorialReviewRepository reviewRepository;
    private final EditionIssueRepository editionIssueRepository;
    private final ArticleFactory articleFactory;

    public ArticleService(
            ArticleRepository articleRepository,
            EditorialReviewRepository reviewRepository,
            EditionIssueRepository editionIssueRepository,
            ArticleFactory articleFactory) {
        this.articleRepository = articleRepository;
        this.reviewRepository = reviewRepository;
        this.editionIssueRepository = editionIssueRepository;
        this.articleFactory = articleFactory;
    }

    public List<Article> findAll() {
        return articleRepository.findAll();
    }

    public Article findById(Long id) {
        Article article = articleRepository.findById(id);
        if (article == null) {
            throw new NoSuchElementException("Статья с ID " + id + " не найдена");
        }
        return article;
    }

    public Article create(String title, String text) {
        Article article = articleFactory.create(title, text);
        article.setStatus("DRAFT");
        return articleRepository.save(article);
    }

    public Article save(Article article) {
        return articleRepository.save(article);
    }

    public void deleteById(Long id) {
        findById(id);

        reviewRepository.findAll().stream()
                .filter(review -> review.getArticle() != null
                        && Objects.equals(review.getArticle().getId(), id))
                .forEach(review -> reviewRepository.deleteById(review.getId()));

        for (EditionIssue issue : editionIssueRepository.findAll()) {
            if (issue.getArticles() != null && issue.getArticles().removeIf(
                    item -> Objects.equals(item.getId(), id))) {
                editionIssueRepository.save(issue);
            }
        }

        articleRepository.deleteById(id);
    }
}