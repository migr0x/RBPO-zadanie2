package ru.mtusi.rbpo.lab2.dto;

public record UserRequest(
        String username,
        String displayName,
        String email
) {
}
