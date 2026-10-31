package ru.mtusi.rbpo.lab2.repository;

import org.springframework.stereotype.Repository;
import ru.mtusi.rbpo.lab2.model.EditorialSection;

@Repository
public class EditorialSectionRepository extends AbstractInMemoryRepository<EditorialSection> {
    public EditorialSectionRepository() {
        super(EditorialSection::id,
                (section, id) -> new EditorialSection(id, section.name(), section.publicationAvailable()));
    }
}
