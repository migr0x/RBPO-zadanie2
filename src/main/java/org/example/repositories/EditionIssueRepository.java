package org.example.repositories;

import org.example.models.EditionIssue;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class EditionIssueRepository {
    private final Map<Long, EditionIssue> storage = new HashMap<>();
    private long idCounter = 1;

    public List<EditionIssue> findAll() { return new ArrayList<>(storage.values()); }
    public EditionIssue findById(Long id) { return storage.get(id); }
    public EditionIssue save(EditionIssue issue) {
        if (issue.getId() == null) issue.setId(idCounter++);
        storage.put(issue.getId(), issue);
        return issue;
    }
    public void deleteById(Long id) { storage.remove(id); }
}