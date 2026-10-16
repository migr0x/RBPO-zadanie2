package org.example.repositories;

import org.example.annotation.Cumponent;
import org.example.models.EditorialSection;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

@Cumponent
@Repository
public class EditorialSectionRepository {
    private final ConcurrentMap<Long, EditorialSection> storage = new ConcurrentHashMap<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    public List<EditorialSection> findAll() {
        return new ArrayList<>(storage.values());
    }

    public EditorialSection findById(Long id) {
        return storage.get(id);
    }

    public EditorialSection save(EditorialSection section) {
        if (section.getId() == null) {
            section.setId(idCounter.getAndIncrement());
        }
        storage.put(section.getId(), section);
        return section;
    }

    public void deleteById(Long id) {
        storage.remove(id);
    }
}