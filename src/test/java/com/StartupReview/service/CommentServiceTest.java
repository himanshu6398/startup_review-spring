package com.StartupReview.service;

import com.StartupReview.models.Comment;
import com.StartupReview.models.Rating;
import com.StartupReview.repository.CommentRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    CommentRepository commentRepository;

    @InjectMocks
    CommentService commentService;

    @Test
    void saveComment() {
        Comment comment = new Comment();
        when(commentRepository.save(comment)).thenReturn(comment);
        assertEquals(comment, commentService.saveComment(comment));
    }

    @Test
    void getCommentById() {
        Comment comment = new Comment();
        comment.setId(1L);
        when(commentRepository.findById(comment.getId())).thenReturn(Optional.of(comment));
        Optional<Comment> result =  commentService.getCommentById(comment.getId());
        Assertions.assertThat(result).isNotEmpty();
    }

    @Test
    void getFirstComments() {
        List<Comment> comments = new ArrayList<>();
        Comment comment = new Comment();
        Rating rating = new Rating();
        rating.setId(1L);
        comment.setRating(rating);
        comments.add(comment);

        when(commentRepository.findByDateTimeOrderByDateTimeAsc(rating.getId(),1L)).thenReturn(comments);
        List<Comment> result = commentService.getFirstComments(rating.getId(),1L);
        assertEquals(comments, result);
    }

    @Test
    void getAllComments() {
        List<Comment> comments = new ArrayList<>();
        Comment comment = new Comment();
        Rating rating = new Rating();
        rating.setId(1L);
        comment.setRating(rating);
        comments.add(comment);

        when(commentRepository.getAllCommentsByRating(rating.getId())).thenReturn(comments);
        List<Comment> result = commentService.getAllComments(rating.getId());
        assertEquals(comments, result);
    }
}