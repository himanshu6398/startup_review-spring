package com.StartupReview.service;

import com.StartupReview.models.Rating;


import com.StartupReview.models.Startup;
import com.StartupReview.payload.response.StartupRatingResponse;
import com.StartupReview.repository.RatingRepository;
import com.StartupReview.repository.StartupRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RatingService {

    @Autowired
    private RatingRepository ratingRepository;

    public RatingService(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    public Rating saveRating(Rating rating){
        return ratingRepository.save(rating);
    }

    public Boolean existByUser_idAndStartup_id(Long user_id, Long startup_id) {
        return ratingRepository.existsByUser_idAndStartup_id(user_id, startup_id);
    }

    public StartupRatingResponse getstartupsRating(long id) {
        long count = ratingRepository.countofRatings(id);
        if(count == 0){
            return new StartupRatingResponse(0,count);
        } else {
            float avgRating = ratingRepository.getAvgStartupRating(id);
            return new StartupRatingResponse( avgRating, count);
        }
    }


    public Optional<Rating> getReviewByStartupIdAndUserId(long user_id, long startup_id) {
        return ratingRepository.FindByUser_idAndStartup_id(user_id,startup_id);
    }

    public Optional<Rating> getRatingById(long ratingId) {
        return ratingRepository.findById(ratingId);
    }

    public Page<Rating> getRatings(Long startupId,Pageable pageable){

            return ratingRepository.FindByStartupOrderByDateTimeDesc(startupId,pageable);

    }


}
