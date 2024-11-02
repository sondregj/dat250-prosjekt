package no.hvl.dat250.polls.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import no.hvl.dat250.polls.Error.OperationFailedError;
import no.hvl.dat250.polls.Error.ResourceNotFoundException;
import no.hvl.dat250.polls.Services.GuestUserService;
import no.hvl.dat250.polls.Services.UserService;
import no.hvl.dat250.polls.Services.VoteOptionService;
import no.hvl.dat250.polls.Services.VoteService;
import no.hvl.dat250.polls.models.User;
import no.hvl.dat250.polls.models.Vote;
import no.hvl.dat250.polls.models.guestUser;

/**
 * VoteController
 * @author Jonas Vestbø
 */
@RestController
@RequestMapping("/api/votes")
public class VoteController {


    @Autowired VoteService service;
    @Autowired UserService userService;
    @Autowired VoteOptionService voService;
    @Autowired GuestUserService guService;

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
            Jwt token = (Jwt) authentication.getPrincipal();
            String username = token.getClaimAsString("sub");
            User user = userService.getUserByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            if (!vote.getUser().equals(user)){
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } else {
            guestUser guest = guService.getGuestById(guestId)
                .orElseThrow(() -> new ResourceNotFoundException("Guest user not found"));
            if (!vote.getGuest().equals(guest)){
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
        System.out.println("CreatedVote: " + createdVote);
        if (authentication == null && guestId == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (authentication != null){
            Jwt token = (Jwt) authentication.getPrincipal();
            String username = token.getClaimAsString("sub");
            User user = userService.getUserByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            createdVote.setUser(user);
            Optional<Vote> existingVote = service.findUserVoteOnPoll(user, createdVote);
            if (existingVote.isPresent()){
                Vote oldVote = existingVote.get();
                Vote vote = service.updateVote(oldVote.getId(), createdVote)
                    .orElseThrow(() -> new OperationFailedError("Could not update Vote"));
                return new ResponseEntity<>(vote, HttpStatus.CREATED);
            }
        } else {
            guestUser guest = guService.getGuestById(guestId)
                .orElseThrow(() -> new ResourceNotFoundException("Guest user not found"));
            createdVote.setGuest(guest);
            Optional<Vote> existingVote = service.findGuestVoteOnPoll(guest, createdVote);
            if (existingVote.isPresent()){
                Vote oldVote = existingVote.get();
                Vote vote = service.updateVote(oldVote.getId(), createdVote)
                    .orElseThrow(() -> new OperationFailedError("Could not update Vote"));
                return new ResponseEntity<>(vote, HttpStatus.CREATED);
            }
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
