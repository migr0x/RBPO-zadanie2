package org.example.services;

import org.example.annotation.Cumponent;
import org.example.factories.EditorialReviewFactory;
import org.example.models.Article;
import org.example.models.EditorialReview;
import org.example.repositories.EditorialReviewRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Cumponent
@Service
public class EditorialReviewService {
    private final EditorialReviewRepository repository;
    private final EditorialReviewFactory factory;

    public EditorialReviewService(
            EditorialReviewRepository repository,
            EditorialReviewFactory factory) {
        this.repository = repository;
        this.factory = factory;
    }

    public List<EditorialReview> findAll() {
        return repository.findAll();
    }

    public EditorialReview findById(Long id) {
        EditorialReview review = repository.findById(id);
        if (review == null) {
            throw new NoSuchElementException(
                    "Редакторская проверка с ID " + id + " не найдена");
        }
        return review;
    }

    public EditorialReview create(
            Article article,
            String decision,
            String notes) {
        EditorialReview review = factory.create();
        review.setArticle(article);
        review.setDecision(decision);
        review.setNotes(notes);
        review.setReviewTime(LocalDateTime.now());
        return repository.save(review);
    }

    public EditorialReview save(EditorialReview review) {
        review.setReviewTime(LocalDateTime.now());
        return repository.save(review);
    }

    public void deleteById(Long id) {
        findById(id);
        repository.deleteById(id);
    }
}