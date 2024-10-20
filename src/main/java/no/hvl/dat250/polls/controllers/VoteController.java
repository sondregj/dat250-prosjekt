package no.hvl.dat250.polls.controllers;

import java.util.List;
import java.util.Optional;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import no.hvl.dat250.polls.Services.VoteService;
import no.hvl.dat250.polls.models.Vote;

/**
 * VoteController
 * @author Jonas Vestbø
 */
@RequestMapping("/api/votes")
public class VoteController {


    @Autowired VoteService service;

    @GetMapping
    public ResponseEntity<List<Vote>> getAllVotes(){
        List<Vote> allVotes = service.getAllVotes();
        if (allVotes == null || allVotes.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(allVotes, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vote> getVoteById(@PathVariable("id") Long id){
        Optional<Vote> retrievedVote = service.getVoteById(id);
        if (retrievedVote.isEmpty()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(retrievedVote.get(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Vote> createVote(@RequestBody Vote createdVote){
        Vote vote = service.addVote(createdVote);
        return new ResponseEntity<>(vote, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Vote> updateVote(@PathVariable("id") Long id,
            Vote updatedVote){
        Optional<Vote> updated = service.updateVote(id, updatedVote);
        if (updated.isEmpty()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(updated.get(), HttpStatus.OK);
    }
}
