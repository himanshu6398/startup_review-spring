package com.StartupReview.service;

import com.StartupReview.models.Startup;
import com.StartupReview.models.User;
import com.StartupReview.payload.response.StartupRatingResponse;
import com.StartupReview.repository.StartupRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StartupService {
    @Autowired
    private StartupRepository startupRepository;

    public StartupService(StartupRepository startupRepository) {
        this.startupRepository = startupRepository;
    }

    public Startup saveStartup(Startup startup){
         return startupRepository.save(startup);
    }
    public Boolean findByName(String name){
        return startupRepository.existsByName(name);
    }

    public Page<Startup> getstartups(Pageable pageable){
        return startupRepository.findAllByOrderByLaunchDateDesc(pageable);
    }
    public Page<Startup >getstartupsFromSearchData(String searchData,Pageable pageable){
        return  startupRepository.findByNameContainingOrDescriptionContaining(searchData,searchData, pageable);
    }

    public Optional<Startup> getstartupsById(long id) {
        return startupRepository.findById(id);
    }

    public void deleteById(long id) {
        startupRepository.deleteById(id);
    }


    public List<Startup> findAll() {
        return startupRepository.findAll();
    }

    public Page<Startup> getStartupsFromTagData(String tag, Pageable paging) {
            return startupRepository.findByTagsContaining(tag,paging);
    }
    public Page<Startup> getStartupsFromTagDataAndSearchData(String searchData,String tag,Pageable pageable){
        return startupRepository.findByNameContainingOrDescriptionContainingOrTagsContaining(searchData,searchData,tag,pageable);
    }

    public List<Startup> findStartupByUser(Long user_id) {
            return startupRepository.findStartupByUser_id(user_id);
    }
}
