package com.StartupReview.controller;

import com.StartupReview.models.Comment;
import com.StartupReview.models.Rating;
import com.StartupReview.models.Startup;
import com.StartupReview.models.User;
import com.StartupReview.security.jwt.AuthEntryPointJwt;
import com.StartupReview.security.jwt.JwtUtils;
import com.StartupReview.security.services.UserDetailsServiceImpl;
import com.StartupReview.service.CommentService;
import com.StartupReview.service.RatingService;
import com.StartupReview.service.StartupService;
import com.StartupReview.service.UserService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(controllers = CommentController.class)
class CommentControllerTest {
    @MockBean
    private UserService userService;

    @MockBean
    private StartupService startupService;

    @MockBean
    RatingService ratingService;

    @MockBean
    CommentService commentService;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;
    @MockBean
    private JwtUtils jwtUtils;
    @MockBean
    private AuthEntryPointJwt authEntryPointJwt;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getFirst3Comments() throws  Exception{
        Date dt = new Date();
        LocalDateTime now = LocalDateTime.now();

        User user = new User(1L,"user1","user1@gmail.com","name","password");
        Startup startup = new Startup(1L,"zoom","video conferencing app",user,dt,now,"video","testLink");
        Rating rating = new Rating(123L,"Test Rating",5.5F,"Test Description",now,startup,user);
        Comment comment = new Comment(10L,"Dummy Description",now,user,rating);

        List<Comment> listOfCommnents = new ArrayList<>();
        listOfCommnents.add(comment);

        Mockito.when(commentService.getFirstComments(1L,3)).thenReturn(listOfCommnents);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/comment/getFirst3Comments?rating_id=1"))
//                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(10)));
    }

    @Test
    void getCommentFromId()throws  Exception {
        Date dt = new Date();
        LocalDateTime now = LocalDateTime.now();

        User user = new User(1L,"user1","user1@gmail.com","name","password");
        Startup startup = new Startup(1L,"zoom","video conferencing app",user,dt,now,"video","testLink");
        Rating rating = new Rating(123L,"Test Rating",5.5F,"Test Description",now,startup,user);
        Comment comment = new Comment(10L,"Dummy Description",now,user,rating);

        Mockito.when(commentService.getCommentById(10L)).thenReturn(Optional.of(comment));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/comment/getCommentFromId?comment_id=10"))
//                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(10)));
    }

    @Test
    void getAllComments() throws  Exception{
        Date dt = new Date();
        LocalDateTime now = LocalDateTime.now();

        User user = new User(1L,"user1","name","user1@gmail.com","password");
        Startup startup = new Startup(1L,"zoom","video conferencing app",user,dt,now,"video","testLink");
        Rating rating = new Rating(123L,"Test Rating",5.5F,"Test Description",now,startup,user);
        Comment comment = new Comment(10L,"Dummy Description",now,user,rating);

        List<Comment> listOfCommnents = new ArrayList<>();
        listOfCommnents.add(comment);

        Mockito.when(commentService.getAllComments(123L)).thenReturn(listOfCommnents);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/comment/getAllComments?rating_id=123"))
//                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(10)));

    }
}