package org.example.services;

import org.example.models.EditorialReview;
import org.example.repositories.EditorialReviewRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EditorialReviewService {
    private final EditorialReviewRepository repository;

    public EditorialReviewService(EditorialReviewRepository repository) {
        this.repository = repository;
    }

    public List<EditorialReview> findAll() { return repository.findAll(); }
    public EditorialReview findById(Long id) { return repository.findById(id); }

    public EditorialReview save(EditorialReview review) {
        // Требование: Текущее время задаётся сервером
        review.setReviewTime(LocalDateTime.now());
        return repository.save(review);
    }

    public void deleteById(Long id) { repository.deleteById(id); }
}