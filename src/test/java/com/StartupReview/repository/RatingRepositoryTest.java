package com.StartupReview.repository;

import com.StartupReview.models.Rating;
import com.StartupReview.models.Startup;
import com.StartupReview.models.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class RatingRepositoryTest {

        @Autowired
        RatingRepository ratingRepository;

        @Autowired
        UserRepository userRepository;

        @Autowired
        StartupRepository startupRepository;

        @Test
        public void shoudSaveRating(){

            Date dt = new Date();
            LocalDateTime now = LocalDateTime.now();

            User user = new User("user1","user1@gmail.com","name","password");
            userRepository.save(user);

            Startup startup = new Startup("zoom","video conferencing app",user,dt,now,"video","testLink");
            startupRepository.save(startup);

            Rating rating = new Rating("Test Rating",5.5F,"Test Description",now,startup,user);

            Rating savedRating = ratingRepository.save(rating);
            assertThat(savedRating).usingRecursiveComparison().ignoringFields("id").isEqualTo(rating);
        }

        @Test
        public void shouldFindRatingByTitle(){
            Date dt = new Date();
            LocalDateTime now = LocalDateTime.now();

            User user = new User("user1","user1@gmail.com","name","password");
            userRepository.save(user);

            Startup startup = new Startup("zoom","video conferencing app",user,dt,now,"video","testLink");
            startupRepository.save(startup);

            Rating rating = new Rating("Test Rating",5.5F,"Test Description",now,startup,user);

            Rating savedRating = ratingRepository.save(rating);

            Optional<Rating> acutalRating = ratingRepository.findByTitle(rating.getTitle());

            assertThat(savedRating.equals(acutalRating.get())).isTrue();
            assertThat(acutalRating.get()).usingRecursiveComparison().isEqualTo(savedRating);

        }
}