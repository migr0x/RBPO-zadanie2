package org.example.factories;

import org.example.annotation.Cumponent;
import org.example.models.EditorialSection;

@Cumponent
public class EditorialSectionFactory {
    public EditorialSection create() {
        return new EditorialSection();
    }

    public EditorialSection create(String name) {
        EditorialSection section = new EditorialSection();
        section.setName(name);
        return section;
    }
}