package com.StartupReview.repository;

import com.StartupReview.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {

    @Query(value = "SELECT * FROM comments WHERE rating_id=?1  order by date_time asc LIMIT ?2", nativeQuery = true)
    List<Comment> findByDateTimeOrderByDateTimeAsc(long rating_id, long limit);

    @Query("SELECT c FROM Comment c WHERE c.rating.id=?1 ")
    List<Comment> getAllCommentsByRating(long rating_id);



}
