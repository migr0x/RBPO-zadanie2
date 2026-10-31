package ru.mtusi.rbpo.lab2.dto;

import ru.mtusi.rbpo.lab2.model.EditionStatus;

import java.time.LocalDate;
import java.util.List;

public record EditionIssueRequest(
        LocalDate date,
        List<Long> articleIds,
        EditionStatus status
) {
}
