package no.hvl.dat250.polls.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import no.hvl.dat250.polls.Error.AccessDeniedException;
import no.hvl.dat250.polls.Error.CommonErrors;
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
    @Autowired SimpMessagingTemplate messagingTemplate;

    //Does not need to be logged in
    @GetMapping
    public ResponseEntity<?> getPolls(){
        List<Poll> retrievedPolls = service.getAllPolls();
        if (retrievedPolls.isEmpty()){
            return new ResponseEntity<>(
                    CommonErrors.POLL_NOT_FOUND,
                    HttpStatus.NOT_FOUND
                    );
        }
        return new ResponseEntity<>(retrievedPolls, HttpStatus.OK);
    }

    //Does not need to be logged in
    @GetMapping("/{id}")
    public ResponseEntity<?> getPollById(@PathVariable("id") Long id){
        Optional<Poll> retrievedPoll = service.getPollById(id);
            
        if (retrievedPoll.isEmpty()){
            return new ResponseEntity<>(
                    new ResourceNotFoundException("Could not find requested poll"),
                    HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(retrievedPoll.get(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> postPoll(@RequestBody Poll poll, Authentication authentication){
        if (authentication == null){
            return new ResponseEntity<>(
                    CommonErrors.NOT_AUTHORIZED,
                    HttpStatus.FORBIDDEN
    // Needs to be logged in
                    );
        }
        Jwt token = (Jwt) authentication.getPrincipal();
        String username = token.getClaimAsString("sub");

        Optional<User> userOPT = UserService.getUserByUsername(username);
        if (userOPT.isEmpty()){
            return new ResponseEntity<>(
                    CommonErrors.USER_NOT_FOUND,
                    HttpStatus.NOT_FOUND
                    );
                                        
        }
        User user = userOPT.get();
        poll.setCreator(user);
        Poll postedPoll = service.addPoll(poll);
        messagingTemplate.convertAndSend("/topic/new-poll", postedPoll);
        return new ResponseEntity<>(postedPoll, HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity<?> removePoll(@RequestBody Poll poll, Authentication authentication){
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String username = jwt.getClaimAsString("sub");
        Optional<Poll> retrievedPoll = service.getPollById(poll.getId());
    
        if (retrievedPoll.isEmpty()){
            return new ResponseEntity<>(new Error("Did not find requested poll"), 
                    HttpStatus.NOT_FOUND);
        }
    
        Poll existingPoll = retrievedPoll.get();
        if (!existingPoll.getCreator().getUsername().equals(username)){
            return new ResponseEntity<>(new Error("You can only delete your own polls"),
                                            HttpStatus.UNAUTHORIZED);
        }
    
        if (!service.deletePoll(retrievedPoll.get())){
            return new ResponseEntity<>(new OperationFailedError("Could not delete poll"), 
                                            HttpStatus.INTERNAL_SERVER_ERROR);
        }
        messagingTemplate.convertAndSend("/topic/delete-poll", retrievedPoll);
        return new ResponseEntity<>(retrievedPoll.get(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> removePollById(@PathVariable Long id, Authentication authentication){
        if (authentication == null){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String username = jwt.getClaimAsString("sub");
        Optional<Poll> pollOPT = service.getPollById(id);

        if (pollOPT.isEmpty()){
            return new ResponseEntity<>(new ResourceNotFoundException("Requested poll not found"),
                    HttpStatus.NOT_FOUND);
        }

        Poll poll = pollOPT.get();

        if (!poll.getCreator().getUsername().equals(username)){
            return new ResponseEntity<>(new AccessDeniedException("You can only delete your own polls"),
                    HttpStatus.BAD_REQUEST);
        }

        if (!service.deletePoll(poll)){
            return new ResponseEntity<>(new OperationFailedError("Could not delete poll"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        messagingTemplate.convertAndSend("/topic/delete-poll", poll.getId());
        return new ResponseEntity<>(poll, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Poll> updateValidUntil(@PathVariable("id") Long id,
            @RequestBody Poll updatedPoll){
            Poll existingPoll = service.getPollById(id)
               .orElseThrow(() -> new ResourceNotFoundException("Could not find poll"));
    
                existingPoll.setValidUntil(updatedPoll.getValidUntil());
                Poll updated = service.updatePoll(id, existingPoll)
                    .orElseThrow(() -> new ResourceNotFoundException("Could not find poll"));

                    return new ResponseEntity<>(updated, HttpStatus.OK);
            }

    //Needs to be logged in // Maybe not a needed method at all? //Only for admin?
    // @PutMapping("/{id}")
    // public ResponseEntity<Poll> updatePoll(@PathVariable("id") Long id, 
    //         @RequestBody Poll updatedPoll,
    //         Authentication authentication){
    //     Jwt token = (Jwt) authentication.getPrincipal();
    //     String username = token.getClaimAsString("sub");
    //     Poll existingPoll = service.getPollById(id)
    //         .orElseThrow(() -> new ResourceNotFoundException("Poll not found"));
    //
    //     if (!existingPoll.getCreator().getUsername().equals(username)){
    //         throw new AccessDeniedException("You can only modify your own poll");
    //     }
    //     Poll updated = service.updatePoll(id, updatedPoll)
    //         .orElseThrow(() -> new OperationFailedError("Failed to update poll"));
    //
    //     return new ResponseEntity<>(updated, HttpStatus.OK);
    //      
    //
    // }
    
}
