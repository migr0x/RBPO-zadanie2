package ru.mtusi.rbpo.lab2.model;

import java.time.LocalDate;
import java.util.List;

public record EditionIssue(
        Long id,
        LocalDate date,
        List<Long> articleIds,
        EditionStatus status
) {
    public EditionIssue {
        articleIds = articleIds == null ? List.of() : List.copyOf(articleIds);
    }
}
