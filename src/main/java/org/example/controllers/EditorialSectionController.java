package org.example.controllers;

import org.example.annotation.Cumponent;
import org.example.facades.EditorialSectionFacade;
import org.example.models.EditorialSection;
import org.example.services.EditorialSectionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Cumponent
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
    public ResponseEntity<EditorialSection> create(
            @RequestBody EditorialSectionFacade facade) {
        EditorialSection section = service.create(
                facade.getName(),
                facade.getAvailableForPublication());
        return ResponseEntity.status(HttpStatus.CREATED).body(section);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EditorialSection> replace(
            @PathVariable Long id,
            @RequestBody EditorialSectionFacade facade) {
        EditorialSection section = service.findById(id);
        section.setName(facade.getName());
        section.setAvailableForPublication(
                Boolean.TRUE.equals(facade.getAvailableForPublication()));
        return ResponseEntity.ok(service.save(section));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EditorialSection> update(
            @PathVariable Long id,
            @RequestBody EditorialSectionFacade facade) {
        EditorialSection section = service.findById(id);
        if (facade.getName() != null) {
            section.setName(facade.getName());
        }
        if (facade.getAvailableForPublication() != null) {
            section.setAvailableForPublication(
                    facade.getAvailableForPublication());
        }
        return ResponseEntity.ok(service.save(section));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}