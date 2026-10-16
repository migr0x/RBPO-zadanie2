
package org.example.controllers;

import org.example.annotation.Cumponent;
import org.example.facades.EditionIssueFacade;
import org.example.models.EditionIssue;
import org.example.services.EditionIssueService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Cumponent
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
    public ResponseEntity<EditionIssue> create(
            @RequestParam(required = false) List<Long> articleIds,
            @RequestBody EditionIssueFacade facade) {
        EditionIssue issue = service.create(facade.getStatus(), articleIds);
        return ResponseEntity.status(HttpStatus.CREATED).body(issue);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EditionIssue> replace(
            @PathVariable Long id,
            @RequestParam(required = false) List<Long> articleIds,
            @RequestBody EditionIssueFacade facade) {
        EditionIssue issue = service.replace(
                id, facade.getStatus(), articleIds);
        return ResponseEntity.ok(issue);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EditionIssue> update(
            @PathVariable Long id,
            @RequestBody EditionIssueFacade facade) {
        EditionIssue issue = service.findById(id);

        if (facade.getStatus() != null) {
            issue.setStatus(facade.getStatus());
        }

        return ResponseEntity.ok(service.save(issue));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
