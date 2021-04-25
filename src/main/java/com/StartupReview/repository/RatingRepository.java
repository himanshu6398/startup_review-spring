package com.StartupReview.repository;

import com.StartupReview.models.Rating;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepository extends JpaRepository<Rating,Long> {

    Page<Rating> findAllByOrderByDateTimeDesc(Pageable pageable);
}
