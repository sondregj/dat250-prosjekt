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

import no.hvl.dat250.polls.Services.VoteOptionService;
import no.hvl.dat250.polls.models.VoteOption;

/**
 * VoteOptionsController 
 * @author Jonas Vestbø
 */
@RequestMapping("/api/voteoptions")
@RestController
public class VoteOptionsController {
    
    @Autowired VoteOptionService service;

    @GetMapping
    public ResponseEntity<List<VoteOption>> getAllVoteOptions(){
        List<VoteOption> allVoteOptions = service.getAllVoteOptions();
        if (allVoteOptions == null || allVoteOptions.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(allVoteOptions, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VoteOption> getVoteOption(@PathVariable("id") Long id){
        Optional<VoteOption> retrievedVoteOption = service.getVoteOptionById(id);
        if (retrievedVoteOption.isEmpty()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(retrievedVoteOption.get(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<VoteOption> addVoteOption(@RequestBody VoteOption voteOption){
        VoteOption savedVoteOption = service.addVoteOption(voteOption);
        return new ResponseEntity<>(savedVoteOption, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<VoteOption> deleteVoteOption(@PathVariable("id") Long id){
        Optional<VoteOption> retrievedOption = service.getVoteOptionById(id);
        if (retrievedOption.isEmpty()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (!service.deleteVoteOptionById(id)){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(retrievedOption.get(), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VoteOption> udpateVoteOption(@PathVariable("id") Long id,
            @RequestBody VoteOption updated){
            Optional<VoteOption> updatedVoteOption = service.updateVoteOption(id, updated);
            if (updatedVoteOption.isEmpty()){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(updatedVoteOption.get(), HttpStatus.OK);
    }
}
