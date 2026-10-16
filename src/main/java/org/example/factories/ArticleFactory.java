package org.example.factories;

import org.example.annotation.Cumponent;
import org.example.models.Article;

@Cumponent
public class ArticleFactory {
    public Article create() {
        return new Article();
    }

    public Article create(
            String title,
            String text) {
        Article article = new Article();
        article.setTitle(title);
        article.setText(text);
        return article;
    }
}