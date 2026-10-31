package ru.mtusi.rbpo.lab2.dto;

public record EditorialSectionRequest(
        String name,
        Boolean publicationAvailable
) {
}
