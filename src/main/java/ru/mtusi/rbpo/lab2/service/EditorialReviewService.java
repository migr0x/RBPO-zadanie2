package ru.mtusi.rbpo.lab2.service;

import org.springframework.stereotype.Service;
import ru.mtusi.rbpo.lab2.dto.EditorialReviewRequest;
import ru.mtusi.rbpo.lab2.exception.ResourceNotFoundException;
import ru.mtusi.rbpo.lab2.model.EditorialReview;
import ru.mtusi.rbpo.lab2.model.ReviewDecision;
import ru.mtusi.rbpo.lab2.repository.ArticleRepository;
import ru.mtusi.rbpo.lab2.repository.EditorialReviewRepository;

import java.time.Clock;
import java.time.Instant;
import java.util.List;

@Service
public class EditorialReviewService {

    private final EditorialReviewRepository reviewRepository;
    private final ArticleRepository articleRepository;
    private final Clock clock;

    public EditorialReviewService(EditorialReviewRepository reviewRepository,
                                  ArticleRepository articleRepository,
                                  Clock clock) {
        this.reviewRepository = reviewRepository;
        this.articleRepository = articleRepository;
        this.clock = clock;
    }

    public List<EditorialReview> findAll() {
        return reviewRepository.findAll();
    }

    public EditorialReview findById(Long id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Editorial review", id));
    }

    public EditorialReview create(EditorialReviewRequest request) {
        if (request.articleId() == null) throw new IllegalArgumentException("articleId is required");
        if (request.decision() == null) throw new IllegalArgumentException("decision is required");
        requireArticle(request.articleId());
        return reviewRepository.save(new EditorialReview(null, request.articleId(),
                request.decision(), request.remarks(), Instant.now(clock)));
    }

    public EditorialReview update(Long id, EditorialReviewRequest request) {
        EditorialReview current = findById(id);
        Long articleId = request.articleId() == null ? current.articleId() : request.articleId();
        ReviewDecision decision = request.decision() == null ? current.decision() : request.decision();
        String remarks = request.remarks() == null ? current.remarks() : request.remarks();
        requireArticle(articleId);
        return reviewRepository.save(new EditorialReview(id, articleId, decision, remarks, Instant.now(clock)));
    }

    public void deleteById(Long id) {
        findById(id);
        reviewRepository.deleteById(id);
    }

    private void requireArticle(Long articleId) {
        if (!articleRepository.existsById(articleId)) {
            throw new ResourceNotFoundException("Article", articleId);
        }
    }
}
