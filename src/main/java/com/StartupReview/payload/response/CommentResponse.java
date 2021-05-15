package com.StartupReview.payload.response;

import com.StartupReview.models.Comment;
import com.StartupReview.models.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public class CommentResponse {
    private Long id;

    private String description;

    private LocalDateTime dateTime;

    private String userName;

    private long userId;

    public CommentResponse(Comment comment,String userName, long  userId) {
        this.id = comment.getId();
        this.description = comment.getDescription();
        this.dateTime = comment.getDateTime();
        this.userName = userName;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
