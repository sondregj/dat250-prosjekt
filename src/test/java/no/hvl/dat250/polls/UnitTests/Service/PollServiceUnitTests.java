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
import no.hvl.dat250.polls.Services.PollService;
import no.hvl.dat250.polls.models.Poll;

/**
 * ServiceTests
 */
@SpringBootTest
@ActiveProfiles("test")
public class PollServiceUnitTests {

    @Autowired PollService pollService;
    @Autowired PollRepository repo;

    @BeforeEach
    void setUp(){
        repo.deleteAll();
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
     */

    @Test
    void testAddPoll(){
        Poll poll1 = new Poll("Poll 1", Instant.now(), Instant.now().plusSeconds(3600));
        poll1 = pollService.addPoll(poll1);
        assertTrue(poll1.getId() != null);
        assertTrue(poll1.getQuestion().equals("Poll 1")); 
    }
    /**
     * Expected result:
     * Deletes a poll
     */

    @Test
    void testDeletePoll(){
        Poll poll1 = new Poll("Poll 1", Instant.now(), Instant.now().plusSeconds(3600));
        poll1 = pollService.addPoll(poll1);

        boolean deleted = pollService.deletePoll(poll1);

        assertTrue(deleted);

        assertTrue(pollService.getPollById(poll1.getId()).isEmpty());

    }

    /**
     * Expected result:
     * Adds a poll 
     * Updated the added poll 
     * The updated poll has the same id as before update 
     * The new entry in the database is equal to the updated Poll
     */

    @Test
    @Transactional
    void updatePoll(){
        // Add a poll
        Poll poll1 = new Poll("Poll 1", Instant.now(), Instant.now().plusSeconds(3600));
        poll1 = pollService.addPoll(poll1);

        // Update the poll
        Poll updatedPoll = new Poll("Poll 2", Instant.now(), Instant.now().plusSeconds(3600));
        Optional<Poll> retrievedUpdated = pollService.updatePoll(poll1.getId(), updatedPoll);
        updatedPoll = retrievedUpdated.get();

        // Retrieve the poll with the id from the database
        Poll retrievedById = pollService.getPollById(retrievedUpdated.get().getId()).get();
        
        // Check that the updatedPoll has the same is as the previously addded poll
        assertTrue(updatedPoll.getId().equals(poll1.getId()));
        // Check that the retrieved poll is equal to the one that is updated to
        assertTrue(retrievedById.equals(updatedPoll));
    }
}
