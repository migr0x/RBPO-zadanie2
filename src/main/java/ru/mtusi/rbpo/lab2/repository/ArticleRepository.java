package ru.mtusi.rbpo.lab2.repository;

import org.springframework.stereotype.Repository;
import ru.mtusi.rbpo.lab2.model.Article;

import java.util.List;

@Repository
public class ArticleRepository extends AbstractInMemoryRepository<Article> {
    public ArticleRepository() {
        super(Article::id, (article, id) -> new Article(id, article.sectionId(), article.authorId(),
                article.title(), article.text(), article.status()));
    }

    public List<Article> findBySectionId(Long sectionId) {
        return findAll().stream().filter(article -> article.sectionId().equals(sectionId)).toList();
    }

    public List<Article> findByAuthorId(Long authorId) {
        return findAll().stream().filter(article -> article.authorId().equals(authorId)).toList();
    }
}
