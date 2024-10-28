package no.hvl.dat250.polls.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import no.hvl.dat250.polls.Error.AccessDeniedException;
import no.hvl.dat250.polls.Error.OperationFailedError;
import no.hvl.dat250.polls.Error.ResourceNotFoundException;
import no.hvl.dat250.polls.Services.PollService;
import no.hvl.dat250.polls.Services.UserService;
import no.hvl.dat250.polls.models.Poll;
import no.hvl.dat250.polls.models.User;

/**
 * PollController
 * @author Jonas Vestbø
 */
@RestController
@RequestMapping("/api/polls")
public class PollController {

    @Autowired 
    PollService service;

    @Autowired
    UserService UserService;

    @GetMapping
    public ResponseEntity<List<Poll>> getPolls(){
        List<Poll> retrievedPolls = service.getAllPolls();
        if (retrievedPolls.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(retrievedPolls, HttpStatus.OK);
    }

    @GetMapping("/id")
    public ResponseEntity<Poll> getPollById(@PathVariable Long id){
        Poll retrievedPoll = service.getPollById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Poll not found"));

        return new ResponseEntity<>(retrievedPoll, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Object> postPoll(@RequestBody Poll poll, Authentication authentication){
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = UserService.getUserByUsername(userDetails.getUsername());

        poll.setCreator(user);
        Poll postedPoll = service.addPoll(poll);
        return new ResponseEntity<>(postedPoll, HttpStatus.CREATED);
    }

    // @DeleteMapping
    // public ResponseEntity<Object> removePoll(@RequestBody Poll poll, Authentication authentication){
    //     UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    //     Optional<Poll> retrievedPoll = service.getPollById(poll.getId());
    //
    //     if (retrievedPoll.isEmpty()){
    //         return new ResponseEntity<>(new Error("Did not find poll"), HttpStatus.NOT_FOUND);
    //     }
    //
    //     Poll existingPoll = retrievedPoll.get();
    //     if (!existingPoll.getCreator().getUsername().equals(userDetails.getUsername())){
    //         return new ResponseEntity<>(new Error("You can only delete your own polls"),
    //                                         HttpStatus.FORBIDDEN);
    //     }
    //
    //     if (!service.deletePoll(retrievedPoll.get())){
    //         return new ResponseEntity<>(new Error("Could not delete poll"), 
    //                                         HttpStatus.INTERNAL_SERVER_ERROR);
    //     }
    //     return new ResponseEntity<>(retrievedPoll.get(), HttpStatus.OK);
    // }

    @DeleteMapping("/{id}")
    public ResponseEntity<Poll> removePollById(@PathVariable Long id, Authentication authentication){
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Poll poll = service.getPollById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Poll not found"));

        if (!poll.getCreator().getUsername().equals(userDetails.getUsername())){
            throw new AccessDeniedException("You can only delete your own polls");
        }

        if (!service.deletePoll(poll)){
            throw new OperationFailedError("Could not delete poll");
        }

        return new ResponseEntity<>(poll, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Poll> updatePoll(@PathVariable("id") Long id, 
            @RequestBody Poll updatedPoll,
            Authentication authentication){
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Poll existingPoll = service.getPollById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Poll not found"));

        if (!existingPoll.getCreator().getUsername().equals(userDetails.getUsername())){
            throw new AccessDeniedException("You can only modify your own poll");
        }
        Poll updated = service.updatePoll(id, updatedPoll)
            .orElseThrow(() -> new OperationFailedError("Failed to update poll"));

        return new ResponseEntity<>(updated, HttpStatus.OK);
           

    }
}
