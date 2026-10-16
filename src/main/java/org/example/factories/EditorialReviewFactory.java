package org.example.factories;

import org.example.annotation.Cumponent;
import org.example.models.EditorialReview;

@Cumponent
public class EditorialReviewFactory {
    public EditorialReview create() {
        return new EditorialReview();
    }
}