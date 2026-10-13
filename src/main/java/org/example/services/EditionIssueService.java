package org.example.services;

import org.example.models.EditionIssue;
import org.example.repositories.EditionIssueRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EditionIssueService {
    private final EditionIssueRepository repository;

    public EditionIssueService(EditionIssueRepository repository) {
        this.repository = repository;
    }

    public List<EditionIssue> findAll() { return repository.findAll(); }
    public EditionIssue findById(Long id) { return repository.findById(id); }

    public EditionIssue save(EditionIssue issue) {
        // Требование: Текущее время задаётся сервером
        issue.setDate(LocalDate.now());
        return repository.save(issue);
    }

    public void deleteById(Long id) { repository.deleteById(id); }
}