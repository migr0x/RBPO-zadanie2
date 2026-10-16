package org.example.facades;

public class EditorialSectionFacade {
    private String name;
    private Boolean availableForPublication;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getAvailableForPublication() {
        return availableForPublication;
    }

    public void setAvailableForPublication(Boolean availableForPublication) {
        this.availableForPublication = availableForPublication;
    }
}