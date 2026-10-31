package ru.mtusi.rbpo.lab2.model;

public record Article(
        Long id,
        Long sectionId,
        Long authorId,
        String title,
        String text,
        ArticleStatus status
) {
}
