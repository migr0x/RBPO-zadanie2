package ru.mtusi.rbpo.lab2.repository;

import org.springframework.stereotype.Repository;
import ru.mtusi.rbpo.lab2.model.EditorialReview;

import java.util.List;

@Repository
public class EditorialReviewRepository extends AbstractInMemoryRepository<EditorialReview> {
    public EditorialReviewRepository() {
        super(EditorialReview::id, (review, id) -> new EditorialReview(id, review.articleId(),
                review.decision(), review.remarks(), review.reviewedAt()));
    }

    public List<EditorialReview> findByArticleId(Long articleId) {
        return findAll().stream().filter(review -> review.articleId().equals(articleId)).toList();
    }

    public void deleteByArticleId(Long articleId) {
        findByArticleId(articleId).forEach(review -> deleteById(review.id()));
    }
}
