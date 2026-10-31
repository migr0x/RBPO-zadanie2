package ru.mtusi.rbpo.lab2.service;

import org.springframework.stereotype.Service;
import ru.mtusi.rbpo.lab2.dto.EditionIssueRequest;
import ru.mtusi.rbpo.lab2.exception.ResourceNotFoundException;
import ru.mtusi.rbpo.lab2.model.EditionIssue;
import ru.mtusi.rbpo.lab2.model.EditionStatus;
import ru.mtusi.rbpo.lab2.repository.ArticleRepository;
import ru.mtusi.rbpo.lab2.repository.EditionIssueRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class EditionIssueService {

    private final EditionIssueRepository issueRepository;
    private final ArticleRepository articleRepository;

    public EditionIssueService(EditionIssueRepository issueRepository,
                               ArticleRepository articleRepository) {
        this.issueRepository = issueRepository;
        this.articleRepository = articleRepository;
    }

    public List<EditionIssue> findAll() {
        return issueRepository.findAll();
    }

    public EditionIssue findById(Long id) {
        return issueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Edition issue", id));
    }

    public EditionIssue create(EditionIssueRequest request) {
        if (request.date() == null) throw new IllegalArgumentException("date is required");
        List<Long> articleIds = request.articleIds() == null ? List.of() : request.articleIds();
        validateArticleIds(articleIds);
        EditionStatus status = request.status() == null ? EditionStatus.DRAFT : request.status();
        return issueRepository.save(new EditionIssue(null, request.date(), articleIds, status));
    }

    public EditionIssue update(Long id, EditionIssueRequest request) {
        EditionIssue current = findById(id);
        LocalDate date = request.date() == null ? current.date() : request.date();
        List<Long> articleIds = request.articleIds() == null ? current.articleIds() : request.articleIds();
        EditionStatus status = request.status() == null ? current.status() : request.status();
        validateArticleIds(articleIds);
        return issueRepository.save(new EditionIssue(id, date, articleIds, status));
    }

    public void deleteById(Long id) {
        findById(id);
        issueRepository.deleteById(id);
    }

    private void validateArticleIds(List<Long> ids) {
        if (ids.stream().anyMatch(id -> id == null)) {
            throw new IllegalArgumentException("articleIds must not contain null values");
        }
        if (ids.stream().distinct().count() != ids.size()) {
            throw new IllegalArgumentException("articleIds must not contain duplicates");
        }
        for (Long articleId : ids) {
            if (!articleRepository.existsById(articleId)) {
                throw new ResourceNotFoundException("Article", articleId);
            }
        }
    }
}
