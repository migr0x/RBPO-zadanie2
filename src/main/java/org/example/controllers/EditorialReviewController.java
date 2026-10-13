package org.example.controllers;

import org.example.models.EditorialReview;
import org.example.services.EditorialReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/editorial-reviews")
public class EditorialReviewController {
    private final EditorialReviewService service;

    public EditorialReviewController(EditorialReviewService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<EditorialReview>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EditorialReview> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<EditorialReview> create(@RequestBody EditorialReview review) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(review));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EditorialReview> update(@PathVariable Long id, @RequestBody EditorialReview review) {
        review.setId(id);
        return ResponseEntity.ok(service.save(review));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}