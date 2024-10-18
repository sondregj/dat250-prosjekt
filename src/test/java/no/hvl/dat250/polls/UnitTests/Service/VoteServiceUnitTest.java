package no.hvl.dat250.polls.UnitTests.Service;

import no.hvl.dat250.polls.Repository.VoteRepository;
import no.hvl.dat250.polls.Services.VoteService;
import no.hvl.dat250.polls.models.Vote;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * VoteServiceUnitTest
 */
@SpringBootTest
@ActiveProfiles("test")
public class VoteServiceUnitTest {

    @Autowired VoteRepository repo;
    @Autowired VoteService service;

    @BeforeEach
    void setUp(){
        repo.deleteAll();
    }

    @Test
    void addVoteTest(){
        Vote vote = new Vote(Instant.now());
        System.out.println("Pre saved vote: " + vote.toString());
        vote = service.addVote(vote);
        Optional<Vote> retrievedVoteOpt = service.getVoteById(vote.getId());

        Vote retrievedVote = retrievedVoteOpt.get();
        assertTrue(retrievedVoteOpt.isPresent());
        assertTrue(vote.equals(retrievedVote));
    }

    @Test
    void deleteVoteByIdTest(){
        Vote vote = new Vote(Instant.now());
        vote = service.addVote(vote);
        
        assertTrue(service.deleteVoteById(vote.getId()));

        assertTrue(service.getVoteById(vote.getId()).isEmpty());
    }

    @Test
    void deleteVoteTest(){
        Vote vote = new Vote(Instant.now());
        vote = service.addVote(vote);
        
        assertTrue(service.deleteVote(vote));

        assertTrue(service.getVoteById(vote.getId()).isEmpty());
    }

    @Test
    void updatedVoteTest(){
        Vote vote = new Vote(Instant.now());
        vote = service.addVote(vote);

        Vote updatedVote = new Vote(Instant.now().plusSeconds(3600));
        
        Optional<Vote> updateVoteOpt = service.updateVote(vote.getId(), updatedVote);

        assertTrue(updateVoteOpt.isPresent());

        updatedVote = updateVoteOpt.get();
        assertTrue(updatedVote.getId().equals(vote.getId()));

        Vote retreievedVote = service.getVoteById(updatedVote.getId()).get();

        assertTrue(retreievedVote.equals(updatedVote));
    }
    
}
