package com.StartupReview.repository;

import com.StartupReview.models.Startup;
import com.StartupReview.models.User;
import com.StartupReview.service.StartupService;
import org.junit.jupiter.api.Test;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@ExtendWith(MockitoExtension.class)
class StartupRepositoryTest {

    @Autowired
    private StartupRepository startupRepositoryTest;

    @Autowired
    private UserRepository userRepositoryTest;

    private StartupService startupService;

    @Test
    void existsByNameTrue() {
        //given error
        User testUser = new User("user1","user1@gmail.com","name","password");
		User userResult = userRepositoryTest.save(testUser);
        Date dt = new Date();
		LocalDateTime now = LocalDateTime.now();

		String name= "zoom";
		Startup startup = new Startup(name,"video conferencing app",testUser,dt,now,"video","TestLink");
        Startup result = startupRepositoryTest.save(startup);
        //when
        Boolean expected = startupRepositoryTest.existsByName(name);
        //then
        assertThat(expected).isTrue();
    }

    @Test
    void existsByNameFalse() {
        //given
        String name= "zoom";
        //when
        Boolean expected = startupRepositoryTest.existsByName(name);
        //then
        assertThat(expected).isFalse();
    }

    @Test
    void findAllByOrderByLaunchDateDesc() {
        //error
        Pageable pageable = PageRequest.of(0,3);
        Page<Startup> s = startupRepositoryTest.findAllByOrderByLaunchDateDesc(pageable);
        assertNotNull(s);
    }

    @Test
    void findByNameContainingOrDescriptionContaining() {
        //error
        String search = "video";
        Pageable pageable = PageRequest.of(0,3);
        Page<Startup> s = startupRepositoryTest.findByNameContainingOrDescriptionContaining(search,search,pageable);
        assertNotNull(s);
    }
    @Test
    void findStartupByUser_id() {
//        User testUser = new User("test","test@gmail.com","name","password");
//        Long id = 1l;
//        testUser.setId(id);
//        userRepositoryTest.save(testUser);
//        Date dt = new Date();
//        LocalDateTime now = LocalDateTime.now();
//
//        String name= "zoom";
//        Startup startup = new Startup(name,"video conferencing app",testUser,dt,now,"video","TestLink");
//        startupRepositoryTest.save(startup);

//        when(startupRepositoryTest.save(startup)).thenReturn(startup);
//        assertThat(startupService.findStartupByUser(1l)).isNotNull();

//        List<Startup> s = startupRepositoryTest.findStartupByUser_id(id);
//        when(startupRepositoryTest.findStartupByUser_id(1l)).thenReturn(s);
//        List<Startup> s1 = startupService.findStartupByUser(1l);
////        assertThat(s).isEmpty();
//        assertEquals(s,s1);
        Long id = 1L;
//        when(startupRepositoryTest.findStartupByUser_id(id)).thenReturn(Stream.of(new Startup("zoom","video calling app")).collect(Collectors.toList()));
//        assertEquals(1, startupService.findStartupByUser(id).size());
//        assertThat(startupService.findStartupByUser(id)).isNotNull();
        List<Startup> s = startupRepositoryTest.findStartupByUser_id(id);
//        assertNotNull(s);
        assertThat(s).isNotNull();
    }

}