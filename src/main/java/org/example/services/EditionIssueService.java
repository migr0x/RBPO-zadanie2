package org.example.services;

import org.example.annotation.Cumponent;
import org.example.factories.EditionIssueFactory;
import org.example.models.Article;
import org.example.models.EditionIssue;
import org.example.repositories.EditionIssueRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Cumponent
@Service
public class EditionIssueService {
    private final EditionIssueRepository repository;
    private final EditionIssueFactory factory;
    private final ArticleService articleService;

    public EditionIssueService(
            EditionIssueRepository repository,
            EditionIssueFactory factory,
            ArticleService articleService) {
        this.repository = repository;
        this.factory = factory;
        this.articleService = articleService;
    }

    public List<EditionIssue> findAll() {
        return repository.findAll();
    }

    public EditionIssue findById(Long id) {
        EditionIssue issue = repository.findById(id);
        if (issue == null) {
            throw new NoSuchElementException(
                    "Выпуск с ID " + id + " не найден");
        }
        return issue;
    }

    public EditionIssue create(String status, List<Long> articleIds) {
        EditionIssue issue = factory.create();
        issue.setDate(LocalDate.now());
        issue.setStatus(status);
        issue.setArticles(resolveArticles(articleIds));
        return repository.save(issue);
    }

    public EditionIssue replace(
            Long id,
            String status,
            List<Long> articleIds) {
        EditionIssue issue = findById(id);
        issue.setStatus(status);
        issue.setArticles(resolveArticles(articleIds));
        return repository.save(issue);
    }

    public EditionIssue save(EditionIssue issue) {
        if (issue.getId() == null && issue.getDate() == null) {
            issue.setDate(LocalDate.now());
        }
        return repository.save(issue);
    }

    public void deleteById(Long id) {
        findById(id);
        repository.deleteById(id);
    }

    private List<Article> resolveArticles(List<Long> articleIds) {
        List<Article> articles = new ArrayList<>();
        if (articleIds == null) {
            return articles;
        }

        for (Long articleId : articleIds) {
            articles.add(articleService.findById(articleId));
        }

        return articles;
    }
}