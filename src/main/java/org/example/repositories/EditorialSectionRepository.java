package org.example.repositories;

import org.example.models.EditorialSection;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class EditorialSectionRepository {
    private final Map<Long, EditorialSection> storage = new HashMap<>();
    private long idCounter = 1;

    public List<EditorialSection> findAll() { return new ArrayList<>(storage.values()); }
    public EditorialSection findById(Long id) { return storage.get(id); }
    public EditorialSection save(EditorialSection section) {
        if (section.getId() == null) section.setId(idCounter++);
        storage.put(section.getId(), section);
        return section;
    }
    public void deleteById(Long id) { storage.remove(id); }
}