package ru.mtusi.rbpo.lab2.service;

import org.springframework.stereotype.Service;
import ru.mtusi.rbpo.lab2.dto.ArticleRequest;
import ru.mtusi.rbpo.lab2.exception.ResourceNotFoundException;
import ru.mtusi.rbpo.lab2.model.Article;
import ru.mtusi.rbpo.lab2.model.ArticleStatus;
import ru.mtusi.rbpo.lab2.repository.ArticleRepository;
import ru.mtusi.rbpo.lab2.repository.EditionIssueRepository;
import ru.mtusi.rbpo.lab2.repository.EditorialReviewRepository;
import ru.mtusi.rbpo.lab2.repository.EditorialSectionRepository;
import ru.mtusi.rbpo.lab2.repository.UserRepository;

import java.util.List;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final EditorialSectionRepository sectionRepository;
    private final UserRepository userRepository;
    private final EditorialReviewRepository reviewRepository;
    private final EditionIssueRepository issueRepository;

    public ArticleService(ArticleRepository articleRepository,
                          EditorialSectionRepository sectionRepository,
                          UserRepository userRepository,
                          EditorialReviewRepository reviewRepository,
                          EditionIssueRepository issueRepository) {
        this.articleRepository = articleRepository;
        this.sectionRepository = sectionRepository;
        this.userRepository = userRepository;
        this.reviewRepository = reviewRepository;
        this.issueRepository = issueRepository;
    }

    public List<Article> findAll() {
        return articleRepository.findAll();
    }

    public Article findById(Long id) {
        return articleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Article", id));
    }

    public Article create(ArticleRequest request) {
        requireReferenceIds(request.sectionId(), request.authorId());
        validateReferences(request.sectionId(), request.authorId());
        requireText(request.title(), "title");
        requireText(request.text(), "text");
        ArticleStatus status = request.status() == null ? ArticleStatus.DRAFT : request.status();
        validatePublicationAvailability(request.sectionId(), status);
        return articleRepository.save(new Article(null, request.sectionId(), request.authorId(),
                request.title().trim(), request.text().trim(), status));
    }

    public Article update(Long id, ArticleRequest request) {
        Article current = findById(id);
        Long sectionId = request.sectionId() == null ? current.sectionId() : request.sectionId();
        Long authorId = request.authorId() == null ? current.authorId() : request.authorId();
        String title = request.title() == null ? current.title() : request.title().trim();
        String text = request.text() == null ? current.text() : request.text().trim();
        ArticleStatus status = request.status() == null ? current.status() : request.status();

        validateReferences(sectionId, authorId);
        validatePublicationAvailability(sectionId, status);
        if (request.title() != null) requireText(request.title(), "title");
        if (request.text() != null) requireText(request.text(), "text");

        return articleRepository.save(new Article(id, sectionId, authorId, title, text, status));
    }

    public void deleteById(Long id) {
        findById(id);
        reviewRepository.deleteByArticleId(id);
        issueRepository.removeArticleFromAllIssues(id);
        articleRepository.deleteById(id);
    }

    private void requireReferenceIds(Long sectionId, Long authorId) {
        if (sectionId == null) throw new IllegalArgumentException("sectionId is required");
        if (authorId == null) throw new IllegalArgumentException("authorId is required");
    }

    private void validateReferences(Long sectionId, Long authorId) {
        if (!sectionRepository.existsById(sectionId)) {
            throw new ResourceNotFoundException("Editorial section", sectionId);
        }
        if (!userRepository.existsById(authorId)) {
            throw new ResourceNotFoundException("User", authorId);
        }
    }

    private void validatePublicationAvailability(Long sectionId, ArticleStatus status) {
        if (status == ArticleStatus.PUBLISHED) {
            boolean available = sectionRepository.findById(sectionId)
                    .map(section -> section.publicationAvailable())
                    .orElse(false);
            if (!available) {
                throw new IllegalArgumentException("The selected editorial section is not available for publication");
            }
        }
    }

    private void requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " must not be blank");
        }
    }
}
