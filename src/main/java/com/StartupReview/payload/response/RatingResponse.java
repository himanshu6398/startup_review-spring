package com.StartupReview.payload.response;

import com.StartupReview.models.Rating;
import com.StartupReview.models.Startup;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public class RatingResponse {


    private Long id;
    private String title;
    private Float rating;
    private String description;
    private LocalDateTime dateTime;
    private String startup;
    private String userName;

    public RatingResponse(Rating rating, String startup, String userName) {
        this.id = rating.getId();
        this.title = rating.getTitle();
        this.rating = rating.getRating();
        this.description = rating.getDescription();
        this.dateTime = rating.getDateTime();
        this.startup = startup;
        this.userName = userName;
    }

    public String getStartup() {
        return startup;
    }

    public void setStartup(String startup) {
        this.startup = startup;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}
