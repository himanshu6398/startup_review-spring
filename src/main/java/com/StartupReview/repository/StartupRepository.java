package com.StartupReview.repository;


import com.StartupReview.models.Startup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StartupRepository extends JpaRepository<Startup,Long> {

    Boolean existsByName(String name);

    Page<Startup> findAllByOrderByDateTimeDesc(Pageable pageable);
    Page<Startup> findAllByOrderByLaunchDateDesc(Pageable pageable);

    Page<Startup> findByNameContainingOrDescriptionContaining(String name,String description,Pageable pageable);

    Page<Startup> findByTagsContaining(String tag,Pageable pageable);

    Page<Startup> findByNameContainingOrDescriptionContainingOrTagsContaining(String name,String description, String tag,Pageable pageable);


    @Query("SELECT s FROM Startup s WHERE s.user.id=?1 ")
    List<Startup> findStartupByUser_id(Long user_id);


}
