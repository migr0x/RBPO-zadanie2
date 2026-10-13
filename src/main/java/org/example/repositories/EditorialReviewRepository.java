package org.example.repositories;

import org.example.models.EditorialReview;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class EditorialReviewRepository {
    private final Map<Long, EditorialReview> storage = new HashMap<>();
    private long idCounter = 1;

    public List<EditorialReview> findAll() { return new ArrayList<>(storage.values()); }
    public EditorialReview findById(Long id) { return storage.get(id); }
    public EditorialReview save(EditorialReview review) {
        if (review.getId() == null) review.setId(idCounter++);
        storage.put(review.getId(), review);
        return review;
    }
    public void deleteById(Long id) { storage.remove(id); }
}