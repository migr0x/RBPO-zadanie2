package org.example.services;

import org.example.models.EditorialSection;
import org.example.repositories.EditorialSectionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EditorialSectionService {
    private final EditorialSectionRepository repository;

    public EditorialSectionService(EditorialSectionRepository repository) {
        this.repository = repository;
    }

    public List<EditorialSection> findAll() { return repository.findAll(); }
    public EditorialSection findById(Long id) { return repository.findById(id); }
    public EditorialSection save(EditorialSection section) { return repository.save(section); }
    public void deleteById(Long id) { repository.deleteById(id); }
}