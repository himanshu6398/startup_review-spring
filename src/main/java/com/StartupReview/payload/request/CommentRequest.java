package com.StartupReview.payload.request;

import javax.validation.constraints.NotBlank;

public class CommentRequest {
    @NotBlank
    private String rating_id;
    @NotBlank
    private String description;

    public CommentRequest(@NotBlank String rating_id, @NotBlank String description) {
        this.rating_id = rating_id;
        this.description = description;
    }

    public String getRating_id() {
        return rating_id;
    }

    public void setRating_id(String rating_id) {
        this.rating_id = rating_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
