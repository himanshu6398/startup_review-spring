package com.StartupReview.controller;

import com.StartupReview.models.Comment;
import com.StartupReview.models.Rating;
import com.StartupReview.models.Startup;
import com.StartupReview.models.User;
import com.StartupReview.payload.request.RatingRequest;
import com.StartupReview.payload.response.*;
import com.StartupReview.security.services.UserDetailsImpl;

import com.StartupReview.service.RatingService;
import com.StartupReview.service.StartupService;
import com.StartupReview.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/review")

public class RatingController {
    private static final Logger logger = LogManager.getLogger(RatingController.class);

    @Autowired
    StartupService startupService;

    @Autowired
    UserService userService;

    @Autowired
    RatingService ratingService;

    @GetMapping("/getReviewFromStartupAndUser")
    public ResponseEntity<?> getReviewFromStartupAndUser(@RequestParam long user_id,@RequestParam long startup_id){
        Optional <Rating> Rating = ratingService.getReviewByStartupIdAndUserId(user_id,startup_id);
        if(Rating.isPresent()){

            return ResponseEntity.ok(Rating.get());
        } else {
            logger.error("[NO RECORD FOUND] - Review not found");
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Review Not found!!!"));
        }
    }

    @GetMapping("/checkUserWrittenReview")
    public ResponseEntity<?> checkUserWrittenReview(@RequestParam long user_id,@RequestParam long startup_id){
        Boolean result = ratingService.existByUser_idAndStartup_id(user_id,startup_id);
        return ResponseEntity.ok(result);
    }
    @GetMapping("/startupInfo/{id}")
    public ResponseEntity<?> getStartupRating(@PathVariable("id") long id ){
        StartupRatingResponse startupRatingResponse = ratingService.getstartupsRating(id);
        if(startupRatingResponse != null ){
            return ResponseEntity.ok(startupRatingResponse);
        } else {
            logger.error("[NO RECORD FOUND] - Unable to find rating of the review");
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Unable to find rating!!"));
        }
    }
    @PutMapping("/update")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> updateReview(@Valid @RequestBody RatingRequest review, @RequestParam("startupId") String startupId) {
        System.out.println(review.getId());
        UserDetailsImpl userDetails =
                (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findById(userDetails.getId()).orElseThrow(() -> new RuntimeException("Error: User is not found."));
        Startup startup = startupService.getstartupsById(Long.parseLong(startupId)).orElseThrow(() -> new RuntimeException("Error: Startup not found"));

        Rating rating = ratingService.getRatingById(Long.parseLong(review.getId())).orElseThrow(() -> new RuntimeException("Error: Rating not found"));

        rating.setTitle(review.getTitle());
        rating.setRating(Float.parseFloat(review.getRating()));
        rating.setDescription(review.getDescription());
        rating.setUser(user);
        rating.setStartup(startup);
//        rating.setDateTime(LocalDateTime.now());

        Rating result = ratingService.saveRating(rating);
        if(result != null ){
            logger.info("[RECORD UPDATED] - Review Updated sucessfully");
            return ResponseEntity.ok(new MessageResponse("Review Updated sucessfully!"));
        } else {
            logger.error("[UNABLE TO UPDATE RECORD] - Unable to update review");
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Unable to update review!"));
        }

    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> saveReview(@Valid @RequestBody RatingRequest review, @RequestParam("startupId") String startupId) {
        try{
            UserDetailsImpl userDetails =
                    (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = userService.findById(userDetails.getId()).orElseThrow(() -> new RuntimeException("Error: User is not found."));
            Startup startup = startupService.getstartupsById(Long.parseLong(startupId)).orElseThrow(() -> new RuntimeException("Error: Startup not found"));

            Rating rating = new Rating(review.getTitle(),
                                        Float.parseFloat(review.getRating()),
                                        review.getDescription(),
                                        LocalDateTime.now(),startup,user);

            Boolean reviewDone = ratingService.existByUser_idAndStartup_id(user.getId(),startup.getId());
            if(reviewDone){
                logger.info("[RECORD EXISTS] - Review already written by user on this startup " + startup.getName());
                return ResponseEntity.badRequest().body(new MessageResponse("Error: You have already written the review for this startup!! "));
            } else {
                Rating result = ratingService.saveRating(rating);

                if (result != null) {
                    logger.info("[RECORD ADDED] - Review added successfully" + startup.getName());
                    return ResponseEntity.ok(new MessageResponse("Review added successfully!"));
                } else {
                    logger.error("[UNABLE TO ADD RECORD] - Unable to add review to database");
                    return ResponseEntity.badRequest().body(new MessageResponse("Error: Unable to add review!"));
                }
            }


        }catch (Exception e){
            System.out.println(e.getMessage());
            logger.error("[UNABLE TO ADD RECORD] - Unable to add review to database "+e.getMessage());
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Unable to add new review!"));
        }

    }
    @GetMapping("/get")
    public ResponseEntity<?>getStartups(@RequestParam String startupId,@RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "3") int size){
        Pageable paging = PageRequest.of(page, size);
        Page<Rating> listofReviews = ratingService.getRatings(Long.parseLong(startupId),paging);
        ArrayList<RatingResponse> ratingResponses=new ArrayList<RatingResponse>();
        if(listofReviews != null) {
            for (int i = 0; i < listofReviews.getContent().size(); i++) {
                Rating rating = listofReviews.getContent().get(i);
                ratingResponses.add(new RatingResponse(rating, rating.getStartup().getName(), rating.getUser().getName()));

            }
            return ResponseEntity.ok(new PagingResponse(ratingResponses, listofReviews.getTotalElements(), listofReviews.getTotalPages()));

        } else {
            logger.error("[NO RECORD FOUND] - Review info unable to get");
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Unable to get reviews!!"));
        }

//        if(searchData == null){
//            Page<Startup> listofStartups =  startupService.getstartups(paging);
//            return ResponseEntity.ok(listofStartups);
//        } else {
//            Page<Startup> listofStartups =  startupService.getstartupsFromSearchData(searchData,paging);
//            return ResponseEntity.ok(listofStartups);
//        }

    }

    @GetMapping("/")
    public ResponseEntity<?> getRatingsbyId(@RequestParam long id){
        Optional<Rating> rating = ratingService.getRatingById(id);
        if(rating.isPresent()){
            String startup = rating.get().getStartup().getName();
            System.out.println(startup);
            RatingResponse ratingResponse = new RatingResponse(rating.get(),startup,rating.get().getUser().getName());
            return ResponseEntity.ok(ratingResponse);
        } else {
            logger.error("[NO RECORD FOUND] - Review info does not exist");
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Reviews Not found!!!"));
        }
    }

//    @PostMapping("/add")
//    public ResponseEntity<?> saveRating(@RequestBody RatingRequest ratingRequest) {
//
//        return ResponseEntity.ok(new MessageResponse("Review added"));
//    }

}
