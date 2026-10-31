package ru.mtusi.rbpo.lab2.dto;

import ru.mtusi.rbpo.lab2.model.ArticleStatus;

public record ArticleRequest(
        Long sectionId,
        Long authorId,
        String title,
        String text,
        ArticleStatus status
) {
}
