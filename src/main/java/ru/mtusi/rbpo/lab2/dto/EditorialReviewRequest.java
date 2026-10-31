package ru.mtusi.rbpo.lab2.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.mtusi.rbpo.lab2.model.ReviewDecision;

@JsonIgnoreProperties(ignoreUnknown = true)
public record EditorialReviewRequest(
        Long articleId,
        ReviewDecision decision,
        String remarks
) {
}
