package org.example.controllers;

import org.example.models.EditionIssue;
import org.example.services.EditionIssueService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/edition-issues")
public class EditionIssueController {
    private final EditionIssueService service;

    public EditionIssueController(EditionIssueService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<EditionIssue>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EditionIssue> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<EditionIssue> create(@RequestBody EditionIssue issue) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(issue));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EditionIssue> update(@PathVariable Long id, @RequestBody EditionIssue issue) {
        issue.setId(id);
        return ResponseEntity.ok(service.save(issue));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}