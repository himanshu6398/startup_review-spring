package com.StartupReview.service;

import com.StartupReview.models.Startup;
import com.StartupReview.models.User;
import com.StartupReview.repository.StartupRepository;
import com.StartupReview.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class StartupServiceTest {

    @Mock
    private StartupRepository startupRepository;

    private StartupService startupService;

    @Mock
    private UserRepository userRepository;


    @BeforeEach
    void setUp() {
        startupService = new StartupService(startupRepository);
    }

    @Test
    void saveStartup() {
        User user = new User("user1","user1@gmail.com","name","password");
		Date dt = new Date();
		LocalDateTime now = LocalDateTime.now();
		Startup startup = new Startup("zoom","video conferencing app",user,dt,now,"video","testLink");

		when(startupRepository.save(startup)).thenReturn(startup);
		assertEquals(startup, startupService.saveStartup(startup));
    }

    @Test
    void findByNameTrue() {
        User user = new User("user1","user1@gmail.com","name","password");
        Date dt = new Date();
        LocalDateTime now = LocalDateTime.now();

        String name = "zoom";
        Startup startup = new Startup(name,"video conferencing app",user,dt,now,"video","testLink");

        when(startupRepository.existsByName(name)).thenReturn(Boolean.TRUE);
//        Boolean b = startupService.findByName(name);

//        assertThat(b).isTrue();
        assertEquals(Boolean.TRUE, startupService.findByName(name));
    }

    @Test
    void findByNameFalse() {
        String name = "zoom";
        when(startupRepository.existsByName(name)).thenReturn(Boolean.FALSE);
        Boolean b = startupService.findByName(name);

        assertThat(b).isFalse();
    }

    @Test
    void getstartups() {
        Pageable pageable = PageRequest.of(0,3);
//        startupService.getstartups(pageable);
//        verify(startupRepository).findAllByOrderByLaunchDateDesc(pageable);

        Page<Startup> startupPage = Mockito.mock(Page.class);
        when(startupRepository.findAllByOrderByLaunchDateDesc(pageable)).thenReturn(startupPage);
        Page<Startup> result = startupService.getstartups(pageable);
        assertEquals(startupPage, result);
    }


    @Test
    void getstartupsFromSearchData() {
        String search="zoom";
        Pageable pageable = PageRequest.of(0,3);
        startupService.getstartupsFromSearchData(search, pageable);
        verify(startupRepository).findByNameContainingOrDescriptionContaining(search, search, pageable);

        Page<Startup> startupPage = Mockito.mock(Page.class);
        when(startupRepository.findByNameContainingOrDescriptionContaining(search, search, pageable)).thenReturn(startupPage);
        Page<Startup> result = startupService.getstartupsFromSearchData(search, pageable);
        assertEquals(startupPage, result);

    }

    @Test
    void getStartupsFromTagData() {
        String tag ="video";
        Pageable pageable = PageRequest.of(0,3);
        Page<Startup> startupPage = Mockito.mock(Page.class);

        when(startupRepository.findByTagsContaining(tag, pageable)).thenReturn(startupPage);
        Page<Startup> result = startupService.getStartupsFromTagData(tag, pageable);
        assertEquals(startupPage, result);
    }


    @Test
    void getStartupsFromTagDataAndSearchData() {
        String search ="zoom";
        String tag ="video";
        Pageable pageable = PageRequest.of(0,3);
        startupService.getStartupsFromTagDataAndSearchData(search,tag,pageable);
        verify(startupRepository).findByNameContainingOrDescriptionContainingOrTagsContaining(search,search,tag,pageable);

        Page<Startup> startupPage = Mockito.mock(Page.class);
        when(startupRepository.findByNameContainingOrDescriptionContainingOrTagsContaining(search,search,tag,pageable))
                .thenReturn(startupPage);
        Page<Startup> result = startupService.getStartupsFromTagDataAndSearchData(search,tag,pageable);
        assertEquals(startupPage,result);
    }

    @Test
    void getstartupsById() {
        User testUser = new User("user1","user1@gmail.com","name","password");
//        userRepository.save(testUser);

        Date dt = new Date();
        LocalDateTime now = LocalDateTime.now();

        Startup startup = new Startup("dummy","dummy",testUser,dt,now,"dummy","testLink");

        startup.setId(1l);

        when(startupRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(startup));

        Optional<Startup> s = startupService.getstartupsById(startup.getId());

        assertNotNull(s);
    }

    @Test
    void deleteById() {
        startupService.deleteById(1l);
        verify(startupRepository).deleteById(1l);
    }

    @Test
    void findAll() {
        //when
        startupService.findAll();
        //then
        verify(startupRepository).findAll();
    }

    @Test
    void findStartupByUser() {
        List<Startup> startups = new ArrayList<>();
        Startup startup = new Startup();

        User user = new User();
        user.setId(1L);
        startup.setUser(user);

        startups.add(startup);

        when(startupRepository.findStartupByUser_id(user.getId())).thenReturn(startups);
        List<Startup> result = startupService.findStartupByUser(user.getId());
        assertEquals(startups, result);
    }
}