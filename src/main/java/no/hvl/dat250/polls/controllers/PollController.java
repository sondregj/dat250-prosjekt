package no.hvl.dat250.polls.controllers;

import java.util.List;
import no.hvl.dat250.polls.Services.PollService;
import no.hvl.dat250.polls.models.Poll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/polls")
public class PollController {

    @Autowired
    private PollService pollService;

    @GetMapping("")
    public ResponseEntity<List<Poll>> getAllPolls() {
        return ResponseEntity.ok(pollService.getAllPolls());
    }

    @PostMapping("")
    public ResponseEntity<Poll> addPoll(Poll poll) {
        return ResponseEntity.ok(pollService.addPoll(poll));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Poll> getPollById(Long id) {
        return ResponseEntity.ok(pollService.getPollById(id).orElse(null));
    }
}
