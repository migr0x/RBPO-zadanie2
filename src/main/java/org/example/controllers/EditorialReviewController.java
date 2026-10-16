package org.example.controllers;

import org.example.annotation.Cumponent;
import org.example.facades.EditorialReviewFacade;
import org.example.models.Article;
import org.example.models.EditorialReview;
import org.example.services.ArticleService;
import org.example.services.EditorialReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Cumponent
@RestController
@RequestMapping("/api/editorial-reviews")
public class EditorialReviewController {
    private final EditorialReviewService service;
    private final ArticleService articleService;

    public EditorialReviewController(
            EditorialReviewService service,
            ArticleService articleService) {
        this.service = service;
        this.articleService = articleService;
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
    public ResponseEntity<EditorialReview> create(
            @RequestParam Long articleId,
            @RequestBody EditorialReviewFacade facade) {
        Article article = articleService.findById(articleId);
        EditorialReview review = service.create(
                article,
                facade.getDecision(),
                facade.getNotes());
        return ResponseEntity.status(HttpStatus.CREATED).body(review);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EditorialReview> replace(
            @PathVariable Long id,
            @RequestParam Long articleId,
            @RequestBody EditorialReviewFacade facade) {
        Article article = articleService.findById(articleId);
        EditorialReview review = service.findById(id);
        review.setArticle(article);
        review.setDecision(facade.getDecision());
        review.setNotes(facade.getNotes());
        return ResponseEntity.ok(service.save(review));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EditorialReview> update(
            @PathVariable Long id,
            @RequestParam(required = false) Long articleId,
            @RequestBody EditorialReviewFacade facade) {
        EditorialReview review = service.findById(id);
        if (articleId != null) {
            review.setArticle(articleService.findById(articleId));
        }
        if (facade.getDecision() != null) {
            review.setDecision(facade.getDecision());
        }
        if (facade.getNotes() != null) {
            review.setNotes(facade.getNotes());
        }
        return ResponseEntity.ok(service.save(review));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}