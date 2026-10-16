package org.example.repositories;

import org.example.annotation.Cumponent;
import org.example.models.EditionIssue;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

@Cumponent
@Repository
public class EditionIssueRepository {
    private final ConcurrentMap<Long, EditionIssue> storage = new ConcurrentHashMap<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    public List<EditionIssue> findAll() {
        return new ArrayList<>(storage.values());
    }

    public EditionIssue findById(Long id) {
        return storage.get(id);
    }

    public EditionIssue save(EditionIssue issue) {
        if (issue.getId() == null) {
            issue.setId(idCounter.getAndIncrement());
        }
        storage.put(issue.getId(), issue);
        return issue;
    }

    public void deleteById(Long id) {
        storage.remove(id);
    }
}