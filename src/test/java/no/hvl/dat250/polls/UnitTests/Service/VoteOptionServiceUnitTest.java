package no.hvl.dat250.polls.UnitTests.Service;

import no.hvl.dat250.polls.Repository.VoteOptionRepository;
import no.hvl.dat250.polls.Repository.PollRepository;
import no.hvl.dat250.polls.Repository.VoteRepository;
import no.hvl.dat250.polls.Services.PollService;
import no.hvl.dat250.polls.Services.VoteOptionService;
import no.hvl.dat250.polls.Services.VoteService;
import no.hvl.dat250.polls.models.Poll;
import no.hvl.dat250.polls.models.Vote;
import no.hvl.dat250.polls.models.VoteOption;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


/**
 * VoteOptionServiceUnitTest
 */
@SpringBootTest
@ActiveProfiles("test")
public class VoteOptionServiceUnitTest {
    
    @Autowired VoteOptionRepository repo;
    @Autowired VoteOptionService service;
    @Autowired VoteService vService;
    @Autowired VoteRepository vRepo;
    @Autowired PollService pService;
    @Autowired PollRepository pRepo;

    @BeforeEach
    void setUp(){
        repo.deleteAll();;
        vRepo.deleteAll();
        pRepo.deleteAll();
    }

    @Test
    void testAddVoteOption(){
        VoteOption option = new VoteOption("Test", 1);
        option = service.addVoteOption(option);

        VoteOption retrieved = service.getVoteOptionById(option.getId()).get();
        
        assertTrue(option.equals(retrieved));
    }

    @Test
    void testDeleteVoteOption(){
        VoteOption option = new VoteOption("Test", 1);
        Vote vote = new Vote(Instant.now());
        option = service.addVoteOption(option);
        vote.setVoteOption(option);
        vote = vService.addVote(vote);


        VoteOption retrieved = service.getVoteOptionById(option.getId()).get();
        assertTrue(retrieved.getVotes().contains(vote));

        assertTrue(service.deleteVoteOption(retrieved));
        assertTrue(service.getVoteOptionById(retrieved.getId()).isEmpty());
        assertTrue(vService.getVoteById(vote.getId()).isEmpty());
    }

    @Test
    void testDeleteVoteOptionById(){
        VoteOption option = new VoteOption("Test", 1);
        Vote vote = new Vote(Instant.now());
        option = service.addVoteOption(option);
        vote.setVoteOption(option);
        vote = vService.addVote(vote);

        VoteOption retrieved = service.getVoteOptionById(option.getId()).get();
        assertTrue(retrieved.getVotes().contains(vote));

        assertTrue(service.deleteVoteOptionById(retrieved.getId()));
        assertTrue(service.getVoteOptionById(retrieved.getId()).isEmpty());
        assertTrue(vService.getVoteById(vote.getId()).isEmpty());
    }

    @Test
    void testUpdateVoteOption(){
        Poll poll = new Poll("Q1", Instant.now(), Instant.now().plusSeconds(3600));
        Poll poll2 = new Poll("Q2", Instant.now(), Instant.now().plusSeconds(3600));
        poll = pService.addPoll(poll);
        poll2 = pService.addPoll(poll2);
        VoteOption option = new VoteOption("Test", 1);
        option.setPoll(poll);
        option = service.addVoteOption(option);
        Vote vote = new Vote(Instant.now());
        vote.setVoteOption(option);
        vote = vService.addVote(vote);

        VoteOption originalRetrieve = service.getVoteOptionById(option.getId()).get();
        assertTrue(originalRetrieve.getVotes().contains(vote));

        VoteOption updated = new VoteOption("Updated", 2);
        updated.setPoll(poll2);
        Optional<VoteOption> updatedOPT = service.updateVoteOption(option.getId(), updated);

        assertTrue(updatedOPT.isPresent());

        updated = updatedOPT.get();
        assertTrue(updated.getId().equals(originalRetrieve.getId()));
        assertTrue(updated.getPoll().equals(poll2));
        assertTrue(updated.getVotes().size() == 1);
        VoteOption retrieved = service.getVoteOptionById(updated.getId()).get();

        assertTrue(updated.equals(retrieved));
    }
}
