package com.StartupReview.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(	name = "ratings")
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max=255)
    private String title;

    private Float rating;

    @NotBlank
    @Column(columnDefinition = "TEXT")
    private String description;


    @Column
    private LocalDateTime dateTime;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "startup_id")
    private Startup startup;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @JsonIgnore
    @OneToMany(mappedBy = "rating", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comment> comments;



    public Rating(@NotBlank String title, @NotBlank Float rating, @NotBlank String description, @NotBlank LocalDateTime dateTime, @NotBlank Startup startup, @NotBlank User user) {
        this.title = title;
        this.rating = rating;
        this.description = description;
        this.dateTime = dateTime;
        this.startup = startup;
        this.user = user;
    }

    public Rating(Long id, @NotBlank String title, Float rating, @NotBlank String description, LocalDateTime dateTime, Startup startup, User user) {
        this.id = id;
        this.title = title;
        this.rating = rating;
        this.description = description;
        this.dateTime = dateTime;
        this.startup = startup;
        this.user = user;
    }

    public Rating() { }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rating rating1 = (Rating) o;
        return id.equals(rating1.id) && title.equals(rating1.title) && rating.equals(rating1.rating);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, rating);
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

    public Startup getStartup() {
        return startup;
    }

    public void setStartup(Startup startup) {
        this.startup = startup;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
