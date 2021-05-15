package com.StartupReview.repository;

import com.StartupReview.models.Rating;
import com.StartupReview.models.Startup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating,Long> {



    Boolean existsByUser_idAndStartup_id(Long user_id, Long startup_id);

    @Query("SELECT count(r) FROM Rating r WHERE r.startup.id=?1")
    long countofRatings(long id);

    @Query("SELECT AVG(r.rating) FROM Rating r WHERE r.startup.id=?1")
    float getAvgStartupRating(long id);

    @Query("SELECT r FROM Rating r WHERE r.user.id=?1 AND r.startup.id=?2")
    Optional<Rating> FindByUser_idAndStartup_id(Long user_id,Long startup_id);

    Page<Rating> findAllByOrderByDateTimeDesc(Pageable pageable);

    @Query("SELECT r FROM Rating r WHERE r.startup.id=?1 order by r.dateTime desc ")
    Page<Rating> FindByStartupOrderByDateTimeDesc(Long startupId, Pageable pageable);

    Optional<Rating> findByTitle(String title);

}
