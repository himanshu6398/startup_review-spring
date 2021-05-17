package com.StartupReview.controller;

import com.StartupReview.models.Comment;
import com.StartupReview.models.Rating;
import com.StartupReview.models.Startup;
import com.StartupReview.models.User;
import com.StartupReview.payload.request.CommentRequest;
import com.StartupReview.payload.request.RatingRequest;
import com.StartupReview.payload.response.CommentResponse;
import com.StartupReview.payload.response.MessageResponse;
import com.StartupReview.security.services.UserDetailsImpl;
import com.StartupReview.service.CommentService;
import com.StartupReview.service.RatingService;
import com.StartupReview.service.StartupService;
import com.StartupReview.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/comment")

public class CommentController {
    private static final Logger logger = LogManager.getLogger(RatingController.class);
    @Autowired
    StartupService startupService;

    @Autowired
    RatingService ratingService;

    @Autowired
    UserService userService;

    @Autowired
    CommentService commentService;

    @GetMapping("/getFirst3Comments")
    public ResponseEntity<?> getFirst3Comments(@RequestParam long rating_id){
            List<Comment> listOfCommnents =  commentService.getFirstComments(rating_id,3);
            ArrayList<CommentResponse> commentResponses=new ArrayList<CommentResponse>();

        for (int i = 0; i < listOfCommnents.size(); i++) {
            Comment comment = listOfCommnents.get(i);
            commentResponses.add(new CommentResponse(comment,comment.getUser().getName(),comment.getUser().getId()));
        }
        return ResponseEntity.ok(commentResponses);

    }
    @GetMapping("/getCommentFromId")
    public ResponseEntity<?> getCommentFromId(@RequestParam long comment_id){
        Optional<Comment> comment = commentService.getCommentById(comment_id);

        if(comment.isPresent()){
            return ResponseEntity.ok(comment.get());
        } else {
            logger.error("[NO RECORD FOUND] - Comment not found");
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Comment Not found!!!"));
        }

    }

    @GetMapping("/getAllComments")
    public ResponseEntity<?> getAllComments(@RequestParam long rating_id){

            List<Comment> listOfComments = commentService.getAllComments(rating_id);
            ArrayList<CommentResponse> commentResponses=new ArrayList<CommentResponse>();

            for (int i = 0; i < listOfComments.size(); i++) {
                 Comment comment = listOfComments.get(i);
                 commentResponses.add(new CommentResponse(comment,comment.getUser().getName(),comment.getUser().getId()));
             }
            return ResponseEntity.ok(commentResponses);
    }
    @PostMapping("/add")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> saveComment(@Valid @RequestBody CommentRequest commentRequest){
        UserDetailsImpl userDetails =
                (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findById(userDetails.getId()).orElseThrow(() -> new RuntimeException("Error: User is not found."));
        Rating rating = ratingService.getRatingById(Long.parseLong(commentRequest.getRating_id())).orElseThrow(() -> new RuntimeException("Error: Rating not found"));

        Comment comment = new Comment(commentRequest.getDescription(),LocalDateTime.now(),user,rating);

        Comment result = commentService.saveComment(comment);
        if (result != null) {
            logger.info("[RECORD ADDED] - Comment added successfully" );
            return ResponseEntity.ok(new MessageResponse("Comment added successfully!"));
        } else {
            logger.error("[UNABLE TO ADD RECORD] - Unable to add comment to database");
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Unable to add comment!!"));
        }

    }
    @PutMapping("/update")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> updateComment(@Valid @RequestBody Comment comment) {

        Comment commentFromDB = commentService.getCommentById(comment.getId()).orElseThrow(() -> new RuntimeException("Error: Rating not found"));
        commentFromDB.setDescription(comment.getDescription());

        Comment result = commentService.saveComment(commentFromDB);

        if(result != null ){
            logger.info("[RECORD UPDATED] - Comment Updated sucessfully");
            return ResponseEntity.ok(new MessageResponse("Comment Updated sucessfully!"));
        } else {
            logger.error("[UNABLE TO UPDATE RECORD] - Unable to update comment");
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Unable to update comment!"));
        }
    }


}
