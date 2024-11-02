package no.hvl.dat250.polls.UnitTests.Service;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import jakarta.transaction.Transactional;
import no.hvl.dat250.polls.Repository.PollRepository;
import no.hvl.dat250.polls.Repository.UserRepository;
import no.hvl.dat250.polls.Repository.VoteOptionRepository;
import no.hvl.dat250.polls.Services.PollService;
import no.hvl.dat250.polls.Services.VoteOptionService;
import no.hvl.dat250.polls.models.Poll;
import no.hvl.dat250.polls.models.VoteOption;
import no.hvl.dat250.polls.models.User;

/**
 * ServiceTests
 */
@SpringBootTest
@ActiveProfiles("test")
public class PollServiceUnitTests {

    @Autowired PollService pollService;
    @Autowired PollRepository repo;
    @Autowired VoteOptionRepository vRepo;
    @Autowired VoteOptionService vService;
    @Autowired UserRepository uRepo;

    @BeforeEach
    void setUp(){
        repo.deleteAll();
        vRepo.deleteAll();
        uRepo.deleteAll();
    }


    /**
     * Expected result: 
     * Adds two Polls 
     * Uses the getAllPolls()
     * Gets the two added polls
     */
    @Test
    void testGetAllPolls() {
        Poll poll1 = new Poll("Poll 1", Instant.now(), Instant.now().plusSeconds(3600));
        Poll poll2 = new Poll("Poll 2", Instant.now(), Instant.now().plusSeconds(7200));
        
        pollService.addPoll(poll1);
        pollService.addPoll(poll2);

        List<Poll> polls = pollService.getAllPolls();

        assertEquals(2, polls.size());
    }

    /**
     * Expected result:
     * Add a poll 
     * Retrieves a poll from the database with the id of the created poll 
     * The added poll and the retrieved poll are equeal
     */
    
    @Test
    @Transactional
    void testGetPollById(){
        Poll poll1 = new Poll("Poll 1", Instant.now(), Instant.now().plusSeconds(3600));
        poll1 = pollService.addPoll(poll1);
        Optional<Poll> retrievedPoll = pollService.getPollById(poll1.getId());
        Poll rPoll = retrievedPoll.get();
        assertTrue(!retrievedPoll.isEmpty());
        assertTrue(rPoll.equals(poll1));
    }

    /**
     * Expected result:
     * Adds a poll
     * Adds the accompanying voteoptions
     * Everything is correctly added to the database
     */

    @Test
    void testAddPoll(){
        Poll poll1 = new Poll("Poll 1", Instant.now(), Instant.now().plusSeconds(3600));
        VoteOption option1 = new VoteOption("VO1", 1);
        option1.setPoll(poll1);
        VoteOption option2 = new VoteOption("VO2", 2);
        option2.setPoll(poll1);
        poll1.setVoteOptions(List.of(option1,option2));
        poll1 = pollService.addPoll(poll1);

        //Check that the poll has been added
        Optional<Poll> retrievedPollOpt = pollService.getPollById(poll1.getId());

        assertTrue(retrievedPollOpt.isPresent());

        Poll retrievedPoll = retrievedPollOpt.get();
        assertTrue(poll1.equals(retrievedPoll));

        //Check that the voteoptions have also been added
        List<VoteOption> retrievedOptions = vService.getAllVoteOptions();
        System.out.println("Retrieved options: " + retrievedOptions);
        assertTrue(retrievedOptions.equals(retrievedPoll.getVoteOptions()));
        assertTrue(vService.deleteVoteOptionById(retrievedOptions.get(0).getId()));

        Optional<VoteOption> retrievedOption = vService.getVoteOptionById(retrievedOptions.get(0).getId());
        assertTrue(retrievedOption.isEmpty());
    }
    /**
     * Expected result:
     * Deletes a poll
     * Deletes the Polls corresponding VoteOptions
     */

    @Test
    void testDeletePoll(){
        //Add a poll with two voteoptions
        User user = new User("Jonas", "Jonas@email.com", "123");
        uRepo.save(user);
        Poll poll1 = new Poll("Poll 1", Instant.now(), Instant.now().plusSeconds(3600));
        poll1.setCreator(user);
        VoteOption option1 = new VoteOption("VO1", 1);
        option1.setPoll(poll1);
        VoteOption option2 = new VoteOption("VO2", 2);
        option2.setPoll(poll1);
        poll1.setVoteOptions(List.of(option1,option2));
        poll1 = pollService.addPoll(poll1);

        //Check that the poll and accompanying voteoptions has been added
        Optional<Poll> retrievedPollOpt = pollService.getPollById(poll1.getId());
        assertTrue(retrievedPollOpt.isPresent());
        assertTrue(vService.getAllVoteOptions().size() > 0);

        //Check that the poll and accompanying voteoptions are deleted
        assertTrue(pollService.deletePoll(retrievedPollOpt.get()));
        assertTrue(vService.getAllVoteOptions().isEmpty());
        assertTrue(pollService.getPollById(poll1.getId()).isEmpty());
    }

    /**
     * Expected result:
     * Adds a poll 
     * Updated the added poll 
     * The updated poll has the same id as before update 
     * The new entry in the database is equal to the updated Poll
     * The updated poll has one new and one old voteoption, the new one gets added, old one stays
     * the one not included is deleted
     */

    @Test
    void updatePoll(){
        // Add a poll
        Poll poll1 = new Poll("Poll 1", Instant.now(), Instant.now().plusSeconds(3600));
        VoteOption option1 = new VoteOption("VO1", 1);
        option1.setPoll(poll1);
        VoteOption option2 = new VoteOption("VO2", 2);
        option2.setPoll(poll1);
        poll1.setVoteOptions(List.of(option1,option2));
        poll1 = pollService.addPoll(poll1);

        // Update the poll
        Poll updatedPoll = new Poll("Poll 2", Instant.now(), Instant.now().plusSeconds(3600));
        VoteOption option3 = new VoteOption("3", 3);
        option3.setPoll(updatedPoll);
        option1.setPoll(updatedPoll);
        updatedPoll.setVoteOptions(List.of(option1,option3));
        Optional<Poll> retrievedUpdated = pollService.updatePoll(poll1.getId(), updatedPoll);
        updatedPoll = retrievedUpdated.get();
        option1 = updatedPoll.getVoteOptions().get(0);
        option3 = updatedPoll.getVoteOptions().get(0);

        // Retrieve the poll with the id from the database
        Poll retrievedById = pollService.getPollById(retrievedUpdated.get().getId()).get();
        
        // Check that the updatedPoll has the same id as the previously addded poll
        assertTrue(updatedPoll.getId().equals(poll1.getId()));
        // Check that the retrieved poll is equal to the one that is updated to
        assertTrue(retrievedById.equals(updatedPoll));
        //Check that option3 has been added and option2 has been removed
        List<VoteOption> retrievedOptions = vService.getAllVoteOptions();
        assertTrue(retrievedOptions.contains(option1));
        assertTrue(retrievedOptions.contains(option3));
        assertTrue(retrievedOptions.size() == 2);
    }
}
