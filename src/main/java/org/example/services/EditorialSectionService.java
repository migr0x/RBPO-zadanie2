package org.example.services;

import org.example.annotation.Cumponent;
import org.example.factories.EditorialSectionFactory;
import org.example.models.Article;
import org.example.models.EditorialSection;
import org.example.repositories.ArticleRepository;
import org.example.repositories.EditorialSectionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Cumponent
@Service
public class EditorialSectionService {
    private final EditorialSectionRepository repository;
    private final EditorialSectionFactory factory;
    private final ArticleRepository articleRepository;

    public EditorialSectionService(
            EditorialSectionRepository repository,
            EditorialSectionFactory factory,
            ArticleRepository articleRepository) {
        this.repository = repository;
        this.factory = factory;
        this.articleRepository = articleRepository;
    }

    public List<EditorialSection> findAll() {
        return repository.findAll();
    }

    public EditorialSection findById(Long id) {
        EditorialSection section = repository.findById(id);
        if (section == null) {
            throw new NoSuchElementException("Рубрика с ID " + id + " не найдена");
        }
        return section;
    }

    public EditorialSection create(String name, Boolean availableForPublication) {
        EditorialSection section = factory.create(name);
        section.setAvailableForPublication(
                Boolean.TRUE.equals(availableForPublication));
        return repository.save(section);
    }

    public EditorialSection save(EditorialSection section) {
        return repository.save(section);
    }

    public void deleteById(Long id) {
        findById(id);

        for (Article article : articleRepository.findAll()) {
            if (article.getSection() != null
                    && Objects.equals(article.getSection().getId(), id)) {
                article.setSection(null);
                articleRepository.save(article);
            }
        }

        repository.deleteById(id);
    }
}