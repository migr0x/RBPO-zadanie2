package org.example.factories;

import org.example.annotation.Cumponent;
import org.example.models.EditionIssue;

@Cumponent
public class EditionIssueFactory {
    public EditionIssue create() {
        return new EditionIssue();
    }
}