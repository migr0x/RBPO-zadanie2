package ru.mtusi.rbpo.lab2.service;

import org.springframework.stereotype.Service;
import ru.mtusi.rbpo.lab2.dto.EditorialSectionRequest;
import ru.mtusi.rbpo.lab2.exception.ResourceNotFoundException;
import ru.mtusi.rbpo.lab2.model.EditorialSection;
import ru.mtusi.rbpo.lab2.repository.ArticleRepository;
import ru.mtusi.rbpo.lab2.repository.EditorialSectionRepository;

import java.util.List;

@Service
public class EditorialSectionService {

    private final EditorialSectionRepository sectionRepository;
    private final ArticleRepository articleRepository;
    private final ArticleService articleService;

    public EditorialSectionService(EditorialSectionRepository sectionRepository,
                                   ArticleRepository articleRepository,
                                   ArticleService articleService) {
        this.sectionRepository = sectionRepository;
        this.articleRepository = articleRepository;
        this.articleService = articleService;
    }

    public List<EditorialSection> findAll() {
        return sectionRepository.findAll();
    }

    public EditorialSection findById(Long id) {
        return sectionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Editorial section", id));
    }

    public EditorialSection create(EditorialSectionRequest request) {
        requireName(request.name());
        boolean available = request.publicationAvailable() != null && request.publicationAvailable();
        return sectionRepository.save(new EditorialSection(null, request.name().trim(), available));
    }

    public EditorialSection update(Long id, EditorialSectionRequest request) {
        EditorialSection current = findById(id);
        String name = request.name() == null ? current.name() : request.name().trim();
        boolean available = request.publicationAvailable() == null
                ? current.publicationAvailable() : request.publicationAvailable();
        if (request.name() != null) requireName(request.name());
        return sectionRepository.save(new EditorialSection(id, name, available));
    }

    public void deleteById(Long id) {
        findById(id);
        articleRepository.findBySectionId(id).stream()
                .map(article -> article.id())
                .toList()
                .forEach(articleService::deleteById);
        sectionRepository.deleteById(id);
    }

    private void requireName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name must not be blank");
        }
    }
}
