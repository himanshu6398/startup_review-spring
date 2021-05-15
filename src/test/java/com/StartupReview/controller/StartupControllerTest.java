package com.StartupReview.controller;

import com.StartupReview.models.Startup;
import com.StartupReview.models.User;
import com.StartupReview.security.jwt.AuthEntryPointJwt;
import com.StartupReview.security.jwt.JwtUtils;
import com.StartupReview.security.services.UserDetailsServiceImpl;
import com.StartupReview.service.StartupService;
import com.StartupReview.service.UserService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

@WebMvcTest(controllers = StartupController.class)
class StartupControllerTest {
    @MockBean
    private UserService userService;
    @MockBean
    private StartupService startupService;
    @MockBean
    private UserDetailsServiceImpl userDetailsService;
    @MockBean
    private JwtUtils jwtUtils;
    @MockBean
    private AuthEntryPointJwt authEntryPointJwt;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getStartups() throws  Exception {
        String searchData = "dummy";
        String tagData = "dummy";
        Pageable paging = PageRequest.of(1, 3);
        List<Startup> listOfStartups = new ArrayList<Startup>();
        Page<Startup> pageListofStartups= new PageImpl<>(listOfStartups);
        Mockito.when(startupService.getStartupsFromTagDataAndSearchData(searchData,tagData,paging)).thenReturn(pageListofStartups);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/startup/?seacrchData=dummy&tagData=dummy&page=1&size=3"))
//                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void getStartupbyId() throws Exception{
        Date dt = new Date();
        LocalDateTime now = LocalDateTime.now();

        User user = new User(1L,"user1","user1@gmail.com","name","password");
        Startup startup = new Startup(1L,"zoom","video conferencing app",user,dt,now,"video","testLink");

        Mockito.when(startupService.getstartupsById(1L)).thenReturn(Optional.of(startup));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/startup/1"))
//                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("zoom")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id",Matchers.is(1)));
    }

    @Test
    void displayStartups() throws Exception{
        List<Startup> startups = new ArrayList<>();
        Mockito.when(startupService.findAll()).thenReturn(startups);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/startup/all"))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    @Disabled
    void getStartupByUser() throws Exception{
        Date dt = new Date();
        LocalDateTime now = LocalDateTime.now();

        User user = new User(1L,"user1","user1@gmail.com","name","password");
        Startup startup = new Startup(1L,"zoom","video conferencing app",user,dt,now,"video","testLink");

        List<Startup> startups = new ArrayList<>();
        startups.add(startup);
        Mockito.when(startupService.findStartupByUser(1L)).thenReturn(startups);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/startup/getStartupByUser"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE));


    }
}