package org.example.controllers;

import org.example.models.EditorialSection;
import org.example.services.EditorialSectionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/editorial-sections")
public class EditorialSectionController {
    private final EditorialSectionService service;

    public EditorialSectionController(EditorialSectionService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<EditorialSection>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EditorialSection> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<EditorialSection> create(@RequestBody EditorialSection section) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(section));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EditorialSection> update(@PathVariable Long id, @RequestBody EditorialSection section) {
        section.setId(id);
        return ResponseEntity.ok(service.save(section));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}