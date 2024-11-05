package no.hvl.dat250.polls.controllers;

import java.net.http.HttpResponse;
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

import no.hvl.dat250.polls.Error.CommonErrors;
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

    @GetMapping("/{id}")
    public ResponseEntity<?> getVoteById(@PathVariable("id") Long id){
        Optional<Vote> retrievedVote = service.getVoteById(id);
        if (retrievedVote.isEmpty()){
            return new ResponseEntity<>(
                    CommonErrors.VOTE_NOT_FOUND,
                    HttpStatus.NOT_FOUND
                    );
        }
        return new ResponseEntity<>(retrievedVote.get(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeVote(@PathVariable("id") Long id,
            @RequestHeader(value = "GuestId", required = false) String guestId,
            Authentication authentication){
        if (authentication == null && guestId == null){
            return new ResponseEntity<>(CommonErrors.NOT_AUTHORIZED, 
                                            HttpStatus.BAD_REQUEST);
        }

        Optional<Vote> voteOPT = service.getVoteById(id);

        if (voteOPT.isEmpty()){
            return new ResponseEntity<>(CommonErrors.VOTE_NOT_FOUND,
                                        HttpStatus.NOT_FOUND);
        }

        Vote vote = voteOPT.get();

        if (authentication != null){
            Jwt token = (Jwt) authentication.getPrincipal();
            String username = token.getClaimAsString("sub");
            Optional<User> userOPT = userService.getUserByUsername(username);
            if (userOPT.isEmpty()){
                return new ResponseEntity<>(CommonErrors.USER_NOT_FOUND,
                                            HttpStatus.NOT_FOUND);
            }
            User user = userOPT.get();
            if (!vote.getUser().equals(user)){
                return new ResponseEntity<>(
                        CommonErrors.WRONG_USER,
                        HttpStatus.FORBIDDEN);
            }
        } else {
            Optional<guestUser> guestOPT = guService.getCheckAndExtendById(guestId);
            if (guestOPT.isEmpty()){
                return new ResponseEntity<>(
                        CommonErrors.USER_NOT_FOUND,
                        HttpStatus.BAD_REQUEST
                        );
            }
            guestUser guest = guestOPT.get();
            if (!vote.getGuest().equals(guest)){
                return new ResponseEntity<>(
                        CommonErrors.WRONG_USER,
                        HttpStatus.FORBIDDEN
                        );
            }
        }

       if (!service.deleteVoteById(id)){
            return new ResponseEntity<>(
                    CommonErrors.NOT_DELETED,
                    HttpStatus.INTERNAL_SERVER_ERROR
                    );
       }
       return new ResponseEntity<>(vote,HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createVote(@RequestBody Vote createdVote,
            @RequestHeader(value = "GuestId", required = false) 
            String guestId,
            Authentication authentication){
        //Checks that there is some form of authentication
        if (authentication == null && guestId == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        //Handles the request if the auth is a JWT token
        if (authentication != null){
            Jwt token = (Jwt) authentication.getPrincipal();
            String username = token.getClaimAsString("sub");
            Optional<User> userOpt = userService.getUserByUsername(username);
            if (userOpt.isEmpty()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResourceNotFoundException("Could not find user with username: " + username));
            }
            User user = userOpt.get();
            createdVote.setUser(user);
            Optional<Vote> existingVote = service.findUserVoteOnPoll(user, createdVote);
            //If the user already has a vote on this poll
            if (existingVote.isPresent()){
                Vote oldVote = existingVote.get();
                Vote vote = service.updateVote(oldVote.getId(), createdVote)
                    .orElseThrow(() -> new OperationFailedError("Could not update Vote"));
                return new ResponseEntity<>(vote, HttpStatus.CREATED);
            }
            //Handles the request if the authentication is a guest-id header
        } else {
            Optional<guestUser> guestUser = guService.getCheckAndExtendById(guestId);
            if (guestUser.isEmpty()){
                return new ResponseEntity<>(
                        CommonErrors.WRONG_USER,
                        HttpStatus.NOT_FOUND
                        );
            }
            guestUser guest = guestUser.get();
            createdVote.setGuest(guest);
            Optional<Vote> existingVote = service.findGuestVoteOnPoll(guest, createdVote);
            //If the guestid already has a vote on this poll
            if (existingVote.isPresent()){
                Vote oldVote = existingVote.get();
                Vote vote = service.updateVote(oldVote.getId(), createdVote)
                    .orElseThrow(() -> new OperationFailedError("Could not update Vote"));
                return new ResponseEntity<>(vote, HttpStatus.CREATED);
            }
        }

        Vote vote = service.addVote(createdVote);
        if (vote == null){
            return new ResponseEntity<>(
                    CommonErrors.POLL_EXPIRED,
                    HttpStatus.NOT_FOUND
                    );
        }
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
