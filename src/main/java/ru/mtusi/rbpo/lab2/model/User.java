package ru.mtusi.rbpo.lab2.model;

public record User(
        Long id,
        String username,
        String displayName,
        String email
) {
}
