package no.hvl.dat250.polls.UnitTests.Service;

import no.hvl.dat250.polls.Repository.VoteOptionRepository;
import no.hvl.dat250.polls.Services.VoteOptionService;
import no.hvl.dat250.polls.models.VoteOption;

import static org.junit.jupiter.api.Assertions.assertTrue;

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

    @BeforeEach
    void setUp(){
        repo.deleteAll();;
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
        option = service.addVoteOption(option);

        VoteOption retrieved = service.getVoteOptionById(option.getId()).get();
        assertTrue(service.deleteVoteOption(retrieved));
        assertTrue(service.getVoteOptionById(retrieved.getId()).isEmpty());
    }

    @Test
    void testDeleteVoteOptionById(){
        VoteOption option = new VoteOption("Test", 1);
        option = service.addVoteOption(option);

        VoteOption retrieved = service.getVoteOptionById(option.getId()).get();
        assertTrue(service.deleteVoteOptionById(retrieved.getId()));
        assertTrue(service.getVoteOptionById(retrieved.getId()).isEmpty());
    }

    @Test
    void testUpdateVoteOption(){
        VoteOption option = new VoteOption("Test", 1);
        option = service.addVoteOption(option);

        VoteOption updated = new VoteOption("Updated", 2);
        Optional<VoteOption> updatedOPT = service.updateVoteOption(option.getId(), updated);

        assertTrue(updatedOPT.isPresent());

        updated = updatedOPT.get();
        assertTrue(updated.getId().equals(option.getId()));

        VoteOption retrieved = service.getVoteOptionById(updated.getId()).get();

        assertTrue(updated.equals(retrieved));
    }
}
