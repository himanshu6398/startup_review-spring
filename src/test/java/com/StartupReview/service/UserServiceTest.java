package com.StartupReview.service;

import com.StartupReview.models.User;
import com.StartupReview.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    UserService userService;

    @BeforeEach
    void setup() {
        userService = new UserService(userRepository);
    }

    @Test
    void findById() {
        User user = new User("test", "test@gmail.com","test","test");
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        assertTrue(userService.findById(user.getId()).isPresent());
//        Optional<User> u = userService.findById(user.getId());
//        assertNotNull(u);
    }

    @Test
    void existsByUsername() {
        String username ="test";
        when(userRepository.existsByUsername(username)).thenReturn(Boolean.FALSE);
//        doReturn(Boolean.FALSE).when(userRepository).existsByUsername(username);
        boolean b = userService.existsByUsername(username);
        assertThat(b).isFalse();

    }

    @Test
    void existsByEmail() {
        User user = new User();
        user.setEmail("test@iiitb.ac.in");
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(Boolean.FALSE);
        boolean result = userService.existsByEmail(user.getEmail());
        assertThat(result).isFalse();
    }

    @Test
    void saveUser() {
        User user = new User("testUser","test@gmail.com","test","password");
//        assertNotNull(userService.saveUser(user));
//        when(userRepository.save(user)).thenReturn(user);
//        assertEquals(user, userService.saveUser(user));
        userService.saveUser(user);
        Mockito.verify(userRepository).save(user);
    }
}