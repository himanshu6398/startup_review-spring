package com.StartupReview.controller;


import com.StartupReview.models.Startup;
import com.StartupReview.models.User;
import com.StartupReview.payload.request.StartupRequest;
import com.StartupReview.payload.response.MessageResponse;
import com.StartupReview.security.services.UserDetailsImpl;
import com.StartupReview.service.StartupService;
import com.StartupReview.service.UserService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/startup")
public class StartupController {
    private static final Logger logger = LogManager.getLogger(StartupController.class);

    @Autowired
    StartupService startupService;

    @Autowired
    UserService userService;

    @GetMapping("/")
    public ResponseEntity<?>getStartups(@RequestParam(required = false) String searchData,@RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "3") int size,@RequestParam(required = false) String tagData){
        Pageable paging = PageRequest.of(page, size);

        if(searchData != null && tagData != null){
            Page<Startup> listofStartups =  startupService.getStartupsFromTagDataAndSearchData(searchData,tagData,paging);
            logger.info("[SEARCH REQUEST] - Requesting from tag and search: ");
            return ResponseEntity.ok(listofStartups);

        }  else if (searchData == null && tagData != null){
                    Page<Startup> listofStartups = startupService.getStartupsFromTagData(tagData,paging);
                    logger.info("[SEARCH REQUEST] - Requesting from tag data "+tagData);
                    return ResponseEntity.ok(listofStartups);
        } else if(searchData != null && tagData == null){
//            Page<Startup> listofStartups =  startupService.getstartupsFromSearchData(searchData,paging);
            Page<Startup> listofStartups =  startupService.getStartupsFromTagDataAndSearchData(searchData,searchData,paging);
            logger.info("[SEARCH REQUEST] - search value: "+searchData);
            return ResponseEntity.ok(listofStartups);
        } else {
            Page<Startup> listofStartups =  startupService.getstartups(paging);
            logger.info("[DATA REQUEST] - Requesting latest startups");
            return ResponseEntity.ok(listofStartups);
        }

    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getStartupbyId(@PathVariable("id") long id ){
        Optional <Startup> startup = startupService.getstartupsById(id);
        if(startup.isPresent()){
            return ResponseEntity.ok(startup.get());
        } else {
            logger.error("[NO RECORD FOUND] - Startup info does not exist");
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Startup Not found!!!"));

        }
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<?> saveStartup(@Valid @RequestBody StartupRequest startupRequest) {
        try {
          //  System.out.println(startup);
            UserDetailsImpl userDetails =
                    (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            if (startupService.findByName(startupRequest.getName().toLowerCase())) {
                logger.error("[RECORD EXISTS] - Startup already exits with this name");
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Start up already exits!!"));
            }
            User user = userService.findById(userDetails.getId()).orElseThrow(() -> new RuntimeException("Error: User is not found."));

            String string = startupRequest.getLaunchDate();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd"); //if month entered in full-text like 'january', use MMMM
            Date launchDate = format.parse(string);

            Startup startup = new Startup(startupRequest.getName().toLowerCase(), startupRequest.getDescription(), user, launchDate, LocalDateTime.now(),startupRequest.getTags());
//            startup.setUser(user);
//            startup.setName(startup.getName().toLowerCase());
//            startup.setDateTime( LocalDateTime.now());

            Startup result = startupService.saveStartup(startup);

            if(result!= null) {
                logger.info("[RECORD ADDED] - Startup added successfully" + result.getName());
                return ResponseEntity.ok(new MessageResponse("Startup added successfully!"));
            } else{
                logger.error("[UNABLE TO ADD RECORD] - Unable to add startup to database");
                return ResponseEntity.badRequest().body(new MessageResponse("Error: Unable to add new startup!"));
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            logger.error("[UNABLE TO ADD RECORD] - Unable to add startup to database "+e.getMessage());
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Unable to add new startup!"));
        }

    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<?> updateStartup(@Valid @PathVariable("id") long id, @RequestBody StartupRequest startupRequest) throws ParseException {

        Optional<Startup> startup = startupService.getstartupsById(id);
        UserDetailsImpl userDetails =
                (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findById(userDetails.getId()).orElseThrow(() -> new RuntimeException("Error: User is not found."));

        String string = startupRequest.getLaunchDate();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd"); //if month entered in full-text like 'january', use MMMM
        Date launchDate = format.parse(string);

        Startup s = startup.get();
        s.setUser(user);
        s.setName(startupRequest.getName().toLowerCase());
        s.setDescription(startupRequest.getDescription());
        s.setLaunchDate(launchDate);
        s.setDateTime(LocalDateTime.now());
        s.setTags(startupRequest.getTags());

        Startup result = startupService.saveStartup(s);

        if (result != null) {
            logger.info("[RECORD UPDATED] - Startup updated successfully" + result.getName());
            return ResponseEntity.ok(new MessageResponse("startup details updated successfully!"));
        } else {
            logger.error("[UNABLE TO UPDATE RECORD] - Unable to update startup to database");
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Unable to update startup details!"));
        }
    }
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<HttpStatus> deleteStartup(@PathVariable("id") long id) {
        try {
            startupService.deleteById(id);
            logger.info("[RECORD DELETED] - Startup deleted successfully");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            logger.error("[UNABLE TO DELETE RECORD] - Unable to delete startup to database "+e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/all")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public List<Startup> displayStartups() {
        logger.info("[RECORDS SHOWN]- All Startup records displayed");
        return startupService.findAll();
    }

    @GetMapping("/getStartupByUser")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('USER')")
    public List<Startup> getStartupByUser(){
        UserDetailsImpl userDetails =
                (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return startupService.findStartupByUser(userDetails.getId());
    }
}
