package no.hvl.dat250.polls.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import no.hvl.dat250.polls.Error.OperationFailedError;
import no.hvl.dat250.polls.Error.ResourceNotFoundException;
import no.hvl.dat250.polls.Services.UserService;
import no.hvl.dat250.polls.Services.VoteService;
import no.hvl.dat250.polls.models.User;
import no.hvl.dat250.polls.models.Vote;

/**
 * VoteController
 * @author Jonas Vestbø
 */
@RestController
@RequestMapping("/api/votes")
public class VoteController {


    @Autowired VoteService service;
    @Autowired UserService userService;

    // @GetMapping
    // public ResponseEntity<List<Vote>> getAllVotes(){
    //     List<Vote> allVotes = service.getAllVotes();
    //     if (allVotes == null || allVotes.isEmpty()){
    //         return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    //     }
    //     return new ResponseEntity<>(allVotes, HttpStatus.OK);
    // }

    @GetMapping("/{id}")
    public ResponseEntity<Vote> getVoteById(@PathVariable("id") Long id){
        Optional<Vote> retrievedVote = service.getVoteById(id);
        if (retrievedVote.isEmpty()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(retrievedVote.get(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Vote> removeVote(@PathVariable("id") Long id,
            @RequestHeader(value = "GuestId", required = false) String guestId,
            Authentication authentication){

        if (authentication == null && guestId == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Vote vote = service.getVoteById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Vote not found"));

        if (authentication != null){
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = userService.getUserByUsername(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            if (!vote.getUser().equals(user)){
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } else {
            if (!vote.getGuestId().equals(guestId)){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }

       if (!service.deleteVoteById(id)){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
       }
       return new ResponseEntity<>(vote,HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Vote> createVote(@RequestBody Vote createdVote,
            @RequestHeader(value = "GuestId", required = false) 
            String guestId,
            Authentication authentication){

        if (authentication == null && guestId == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (authentication != null){
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = userService.getUserByUsername(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            createdVote.setUser(user);
        } else {
            createdVote.setGuestId(guestId);
        }

        Vote vote = service.addVote(createdVote);
        return new ResponseEntity<>(vote, HttpStatus.CREATED);
            }

    // @PutMapping("/{id}")
    // public ResponseEntity<Vote> updateVote(@PathVariable("id") Long id,
    //         @RequestBody Vote updatedVote){
    //     Optional<Vote> updated = service.updateVote(id, updatedVote);
    //     if (updated.isEmpty()){
    //         return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    //     }
    //     return new ResponseEntity<>(updated.get(), HttpStatus.OK);
    // }
}
