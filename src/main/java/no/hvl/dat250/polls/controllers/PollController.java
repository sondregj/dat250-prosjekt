package no.hvl.dat250.polls.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import no.hvl.dat250.polls.Services.PollService;
import no.hvl.dat250.polls.models.Poll;

/**
 * PollController
 * @author Jonas Vestbø
 */
@RestController
@RequestMapping("/api/polls")
public class PollController {

    @Autowired 
    PollService service;

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
        Optional<Poll> retrievedPoll = service.getPollById(id);
        if (retrievedPoll.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(retrievedPoll.get(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Poll> postPoll(@RequestBody Poll poll){
        Poll postedPoll = service.addPoll(poll);
        return new ResponseEntity<>(postedPoll, HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity<Poll> removePoll(@RequestBody Poll poll){
        Optional<Poll> retrievedPoll = service.getPollById(poll.getId());
        if (retrievedPoll.isEmpty() || !service.deletePoll(retrievedPoll.get())){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(retrievedPoll.get(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Poll> removePollById(@PathVariable Long id){
        Optional<Poll> retrievedPoll = service.getPollById(id);
        if (retrievedPoll.isEmpty() || !service.deletePollById(id)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(retrievedPoll.get(), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Poll> updatePoll(@PathVariable("id") Long id, 
            @RequestBody Poll updatedPoll){
            Optional<Poll> updated = service.updatePoll(id, updatedPoll);
            if (updated.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(updated.get(), HttpStatus.OK);
    }
}
