package ru.mtusi.rbpo.lab2.repository;

import org.springframework.stereotype.Repository;
import ru.mtusi.rbpo.lab2.model.EditionIssue;

import java.util.List;

@Repository
public class EditionIssueRepository extends AbstractInMemoryRepository<EditionIssue> {
    public EditionIssueRepository() {
        super(EditionIssue::id, (issue, id) -> new EditionIssue(id, issue.date(), issue.articleIds(), issue.status()));
    }

    public void removeArticleFromAllIssues(Long articleId) {
        for (EditionIssue issue : findAll()) {
            if (issue.articleIds().contains(articleId)) {
                List<Long> remainingIds = issue.articleIds().stream()
                        .filter(id -> !id.equals(articleId))
                        .toList();
                save(new EditionIssue(issue.id(), issue.date(), remainingIds, issue.status()));
            }
        }
    }
}
