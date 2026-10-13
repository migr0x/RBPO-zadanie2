package org.example.models;

public class EditorialSection {
    private Long id;
    private String name;
    private boolean availableForPublication;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAvailableForPublication() {
        return availableForPublication;
    }

    public void setAvailableForPublication(boolean availableForPublication) {
        this.availableForPublication = availableForPublication;
    }
}