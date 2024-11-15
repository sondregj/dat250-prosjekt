package no.hvl.dat250.polls.controllers;

import java.util.List;
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
import no.hvl.dat250.polls.Services.GuestUserService;
import no.hvl.dat250.polls.Services.PollService;
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
    @Autowired PollService pService;

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
            @RequestHeader (value = "GuestId", required = false)
            String guestId, 
            Authentication authentication){
        //Check if the request is authenticated as either guest or user
        if (authentication == null && guestId == null){
            return new ResponseEntity<>(
                    HttpStatus.FORBIDDEN);
        }
        //Handle if the user is logged in 
        if (authentication != null){
            //authenticate login
            Optional<User> userOPT = authenticateUser(authentication);
            if (userOPT.isEmpty()){
                return new ResponseEntity<>(CommonErrors.USER_NOT_FOUND,
                        HttpStatus.FORBIDDEN);
            }
            User user = userOPT.get();
            //Handle vote for a logged in user
            Optional<Vote> vote = service.handleUserVote(user, createdVote);
            if (vote.isEmpty()){
                return new ResponseEntity<>(CommonErrors.COULD_NOT_VOTE,
                        HttpStatus.CONFLICT);
            }
            return new ResponseEntity<>(vote.get(), HttpStatus.OK);
        }else
        //Handle if guestUser
        {
            //authenticate guestuser
            Optional<guestUser> guestOPT = guService.getGuestById(guestId);
            if (guestOPT.isEmpty()){
                return new ResponseEntity<>(CommonErrors.USER_NOT_FOUND,
                        HttpStatus.FORBIDDEN);
            }
            guestUser guest = guestOPT.get();
            //Handle GuestVoting
            Optional<Vote> vote = service.handleGuestVote(guest, createdVote);
            if (vote.isEmpty()){
                return new ResponseEntity<>(CommonErrors.COULD_NOT_VOTE,
                        HttpStatus.CONFLICT);
            }
            return new ResponseEntity<>(vote.get(), HttpStatus.OK);
        }
            }


    @GetMapping
    public ResponseEntity<?> getVotesByUser(Authentication authentication){
       if (authentication == null){
           return new ResponseEntity<>(HttpStatus.FORBIDDEN);
       } 
       Jwt token = (Jwt) authentication.getPrincipal();
       String username = token.getClaimAsString("sub");
       List<Vote> castedVotes = service.getVotesByUser(username);
        if (castedVotes.isEmpty()){
            return new ResponseEntity<>(
                    CommonErrors.POLL_NOT_FOUND,
                    HttpStatus.NO_CONTENT
                    );
        }
       return new ResponseEntity<>(castedVotes, HttpStatus.OK);
    }




    private Optional<User> authenticateUser(Authentication authentication){
        Jwt token = (Jwt) authentication.getPrincipal();
        String username = token.getClaimAsString("sub");
        Optional<User> retrievedUser = userService.getUserByUsername(username);
        return retrievedUser;
    }

    }
