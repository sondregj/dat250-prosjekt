package no.hvl.dat250.polls.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import no.hvl.dat250.polls.Services.GuestUserService;
import no.hvl.dat250.polls.models.guestUser;

/**
 * GuestController
 */
@RequestMapping("/api/guest")
@RestController
@CrossOrigin
public class GuestController {

    @Autowired GuestUserService service;

    @PostMapping
    public ResponseEntity<guestUser> registerUserRepository(){
        guestUser newUser = service.registerGuestUser();
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Boolean> guestUserIsValid(@PathVariable("id") String id){
        Optional<guestUser> getUser = service.getCheckAndExtendById(id);
        return new ResponseEntity<>(getUser.isPresent(), HttpStatus.OK);
    }
}
