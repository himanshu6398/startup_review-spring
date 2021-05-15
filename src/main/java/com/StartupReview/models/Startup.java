package com.StartupReview.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(	name = "startups",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "name")
        })
public class Startup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max=255)
    private String name;

    @NotBlank
    @Column(columnDefinition = "TEXT")
    private String description;

    @NotBlank
    @Column(columnDefinition = "TEXT")
    private String tags;

    @NotBlank
    @Column(columnDefinition = "TEXT")
    private String logoLink;

    @Column
    private Date launchDate;

    @Column
    private LocalDateTime dateTime;

    @JsonIgnore
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @JsonIgnore
    @OneToMany(mappedBy = "startup", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Rating> ratings;

    public Startup() {
    }

    public Startup(@NotBlank String name, @NotBlank String description) {
        this.name = name;
        this.description = description;
    }

    public Startup(@NotBlank String name, @NotBlank String description, User user, Date launchDate, LocalDateTime dateTime,String tags,String logoLink) {
        this.name = name;
        this.description = description;
        this.user = user;
        this.launchDate = launchDate;
        this.dateTime = dateTime;
        this.tags = tags;
        this.logoLink = logoLink;
    }

    public Startup(Long id,@NotBlank String name, @NotBlank String description, User user, Date launchDate, LocalDateTime dateTime,String tags,String logoLink) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.user = user;
        this.launchDate = launchDate;
        this.dateTime = dateTime;
        this.tags = tags;
        this.logoLink = logoLink;
    }



    public List<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Date getLaunchDate() {
        return launchDate;
    }

    public void setLaunchDate(Date launchDate) {
        this.launchDate = launchDate;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getLogoLink() {
        return logoLink;
    }

    public void setLogoLink(String logoLink) {
        this.logoLink = logoLink;
    }
}