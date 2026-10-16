package org.example.controllers;

import org.example.annotation.Cumponent;
import org.example.facades.UserFacade;
import org.example.models.User;
import org.example.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Cumponent
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<User> create(@RequestBody UserFacade facade) {
        User user = service.create(facade.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> replace(
            @PathVariable Long id,
            @RequestBody UserFacade facade) {
        User user = service.findById(id);
        user.setName(facade.getName());
        return ResponseEntity.ok(service.save(user));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<User> update(
            @PathVariable Long id,
            @RequestBody UserFacade facade) {
        User user = service.findById(id);
        if (facade.getName() != null) {
            user.setName(facade.getName());
        }
        return ResponseEntity.ok(service.save(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}