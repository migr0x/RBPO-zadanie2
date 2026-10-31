package ru.mtusi.rbpo.lab2.model;

import java.time.Instant;

public record EditorialReview(
        Long id,
        Long articleId,
        ReviewDecision decision,
        String remarks,
        Instant reviewedAt
) {
}
