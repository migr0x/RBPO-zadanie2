package org.example.controllers;

import org.example.annotation.Cumponent;
import org.example.facades.ArticleFacade;
import org.example.models.Article;
import org.example.services.ArticleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Cumponent
@RestController
@RequestMapping("/api/articles")
public class ArticleController {
    private final ArticleService service;

    public ArticleController(ArticleService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Article>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Article> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<Article> create(@RequestBody ArticleFacade facade) {
        Article article = service.create(facade.getTitle(), facade.getText());
        return ResponseEntity.status(HttpStatus.CREATED).body(article);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Article> replace(
            @PathVariable Long id,
            @RequestBody ArticleFacade facade) {
        Article existing = service.findById(id);
        existing.setTitle(facade.getTitle());
        existing.setText(facade.getText());
        return ResponseEntity.ok(service.save(existing));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Article> update(
            @PathVariable Long id,
            @RequestBody ArticleFacade facade) {
        Article existing = service.findById(id);
        if (facade.getTitle() != null) {
            existing.setTitle(facade.getTitle());
        }
        if (facade.getText() != null) {
            existing.setText(facade.getText());
        }
        return ResponseEntity.ok(service.save(existing));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}