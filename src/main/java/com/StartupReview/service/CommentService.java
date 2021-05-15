package com.StartupReview.service;

import com.StartupReview.models.Comment;
import com.StartupReview.models.Rating;
import com.StartupReview.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {
    @Autowired
    CommentRepository commentRepository;

    public Comment saveComment(Comment comment){
        return commentRepository.save(comment);
    }

    public Optional<Comment> getCommentById(long commentId) {
        return commentRepository.findById(commentId);
    }

    public List<Comment> getFirstComments(long rating_id, long limit){
        return commentRepository.findByDateTimeOrderByDateTimeAsc(rating_id,limit);
    }

    public List<Comment> getAllComments(long rating_id) {
        return commentRepository.getAllCommentsByRating(rating_id);
    }

}
