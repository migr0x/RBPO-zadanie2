package org.example.repositories;

import org.example.annotation.Cumponent;
import org.example.models.EditorialReview;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

@Cumponent
@Repository
public class EditorialReviewRepository {
    private final ConcurrentMap<Long, EditorialReview> storage = new ConcurrentHashMap<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    public List<EditorialReview> findAll() {
        return new ArrayList<>(storage.values());
    }

    public EditorialReview findById(Long id) {
        return storage.get(id);
    }

    public EditorialReview save(EditorialReview review) {
        if (review.getId() == null) {
            review.setId(idCounter.getAndIncrement());
        }
        storage.put(review.getId(), review);
        return review;
    }

    public void deleteById(Long id) {
        storage.remove(id);
    }
}