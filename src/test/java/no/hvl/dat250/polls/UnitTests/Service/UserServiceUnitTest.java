package no.hvl.dat250.polls.UnitTests.Service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import no.hvl.dat250.polls.Repository.UserRepository;
import no.hvl.dat250.polls.Repository.PollRepository;
import no.hvl.dat250.polls.Repository.VoteRepository;
import no.hvl.dat250.polls.Services.UserService;
import no.hvl.dat250.polls.Services.VoteService;
import no.hvl.dat250.polls.Services.PollService;
import no.hvl.dat250.polls.models.Poll;
import no.hvl.dat250.polls.models.User;
import no.hvl.dat250.polls.models.Vote;

/**
 * UserServiceUnitTest
 */

@SpringBootTest
@ActiveProfiles("test")
public class UserServiceUnitTest {

    @Autowired UserRepository repo;
    @Autowired PollRepository pRepo;
    @Autowired VoteRepository vRepo;
    @Autowired UserService service;
    @Autowired PollService pService;
    @Autowired VoteService vService;

    @BeforeEach
    void setUp(){
        repo.deleteAll();
        pRepo.deleteAll();
        vRepo.deleteAll();
    }

    @Test
    public void testAddUser(){
        //Create a user 
        User createdUser = new User("Test", "Test@email.com");
        //Create two new polls
        Poll poll1 = new Poll("Q1", Instant.now(), Instant.now().plusSeconds(3600));
        poll1.setCreator(createdUser); 
        Poll poll2 = new Poll("Q2", Instant.now(), Instant.now().plusSeconds(3600));
        poll2.setCreator(createdUser); 
        createdUser.setCreatedPolls(List.of(poll1, poll2));

        //Create two new votes 
        Vote vote1 = new Vote(Instant.now());
        vote1.setUser(createdUser);
        Vote vote2 = new Vote(Instant.now());
        vote2.setUser(createdUser);
        createdUser.setCastedVotes(List.of(vote1, vote2));
        createdUser = service.addUser(createdUser);

        //Adding the user should also save the User and the two polls in the databas
        //createdUser whould now have an id and have a list of created polls
        assertTrue(createdUser != null);
        assertTrue(createdUser.getId() != null);
        assertTrue(createdUser.getUsername().equals("Test"));
        assertTrue(createdUser.getEmail().equals("Test@email.com"));
        assertTrue(createdUser.getCreatedPolls().size() == 2);
        assertTrue(createdUser.getCreatedPolls().get(0).equals(poll1));
        assertTrue(createdUser.getCreatedPolls().get(1).equals(poll2));
        assertTrue(createdUser.getCastedVotes().size() == 2);
        assertTrue(createdUser.getCastedVotes().get(0).equals(vote1));
        assertTrue(createdUser.getCastedVotes().get(1).equals(vote2));
        
        //Retrieve all polls this should be equal to the polls created by 
        //the new user
        List<Poll> retrievedPolls = pService.getAllPolls();

        //Check that the retrievedlist is equal to the list of the createduser
        assertTrue(createdUser.getCreatedPolls().get(0).equals(retrievedPolls.get(0)));
        assertTrue(createdUser.getCreatedPolls().get(1).equals(retrievedPolls.get(1)));
        assertTrue(createdUser.getCreatedPolls().size() == retrievedPolls.size());
        assertTrue(createdUser.getCreatedPolls().size() == 2);

        //Retrieve all votes this should be equal to the votes created by 
        //the new user
        List<Vote> retrievedVotes = vService.getAllVotes();

        //Check that the retrievedlist is equal to the list of the createduser
        assertTrue(createdUser.getCastedVotes().get(0).equals(retrievedVotes.get(0)));
        assertTrue(createdUser.getCastedVotes().get(1).equals(retrievedVotes.get(1)));
        assertTrue(createdUser.getCastedVotes().size() == retrievedPolls.size());
        assertTrue(createdUser.getCastedVotes().size() == 2);
    }
    
    @Test
    public void testGetUserById(){
        User createdUser = new User("Test", "Test@email.com");
        createdUser = service.addUser(createdUser);

        User retrievedUser = service.getUserById(createdUser.getId()).get();
        assertTrue(retrievedUser.equals(createdUser));
    }

    @Test
    public void testGetAllUsers(){
        User createdUser = new User("Test", "Test@email.com");
        User createdUser2 = new User("Test2", "Test2@email.com");
        createdUser = service.addUser(createdUser);
        createdUser2 = service.addUser(createdUser2);

        List<User> allUsers = service.getAllUsers();

        assertTrue(allUsers.contains(createdUser));
        assertTrue(allUsers.contains(createdUser2));
        assertTrue(allUsers.size() == 2);
    }

    @Test
    public void testDeleteUserById(){
        //Create a user 
        User createdUser = new User("Test", "Test@email.com");
        //Create two new polls
        Poll poll1 = new Poll("Q1", Instant.now(), Instant.now().plusSeconds(3600));
        poll1.setCreator(createdUser); 
        Poll poll2 = new Poll("Q2", Instant.now(), Instant.now().plusSeconds(3600));
        poll2.setCreator(createdUser); 
        createdUser.setCreatedPolls(List.of(poll1, poll2));

        //Create two new votes 
        Vote vote1 = new Vote(Instant.now());
        vote1.setUser(createdUser);
        Vote vote2 = new Vote(Instant.now());
        vote2.setUser(createdUser);
        createdUser.setCastedVotes(List.of(vote1, vote2));
        createdUser = service.addUser(createdUser);

        //Check that the user was creted
        assertTrue(createdUser.getId() != null);
        assertFalse(pService.getAllPolls().isEmpty());
        assertFalse(vService.getAllVotes().isEmpty());

        //Delete the user
        assertTrue(service.deleteUserById(createdUser.getId()));

        //Check that the user, polls and votes was deleted
        assertTrue(service.getUserById(createdUser.getId()).isEmpty());
        assertTrue(pService.getAllPolls().isEmpty());
        assertTrue(vService.getAllVotes().isEmpty());
    }

    @Test
    public void testDeleteUser(){
        //Create a user 
        User createdUser = new User("Test", "Test@email.com");
        //Create two new polls
        Poll poll1 = new Poll("Q1", Instant.now(), Instant.now().plusSeconds(3600));
        poll1.setCreator(createdUser); 
        Poll poll2 = new Poll("Q2", Instant.now(), Instant.now().plusSeconds(3600));
        poll2.setCreator(createdUser); 
        createdUser.setCreatedPolls(List.of(poll1, poll2));

        //Create two new votes 
        Vote vote1 = new Vote(Instant.now());
        vote1.setUser(createdUser);
        Vote vote2 = new Vote(Instant.now());
        vote2.setUser(createdUser);
        createdUser.setCastedVotes(List.of(vote1, vote2));
        createdUser = service.addUser(createdUser);

        //Check that the user, polls and votes is created
        assertTrue(createdUser.getId() != null);
        assertFalse(pService.getAllPolls().isEmpty());
        assertFalse(vService.getAllVotes().isEmpty());

        //Delete the user
        assertTrue(service.deleteUser(createdUser));

        //Check that the user was deleted and that the polls and votes creted 
        //by the user was also deleted
        assertTrue(service.getUserById(createdUser.getId()).isEmpty());
        assertTrue(pService.getAllPolls().isEmpty());
        assertTrue(vService.getAllVotes().isEmpty());
    }

    @Test
    public void testUpdateUser(){
        //Create a user 
        User createdUser = new User("Test", "Test@email.com");
        //Create two new polls
        Poll poll1 = new Poll("Q1", Instant.now(), Instant.now().plusSeconds(3600));
        poll1.setCreator(createdUser); 
        Poll poll2 = new Poll("Q2", Instant.now(), Instant.now().plusSeconds(3600));
        poll2.setCreator(createdUser); 
        createdUser.setCreatedPolls(List.of(poll1, poll2));

        //Create two new votes 
        Vote vote1 = new Vote(Instant.now());
        vote1.setUser(createdUser);
        Vote vote2 = new Vote(Instant.now());
        vote2.setUser(createdUser);
        createdUser.setCastedVotes(List.of(vote1, vote2));
        createdUser = service.addUser(createdUser);

        //Create a new update user
        User updatedUser = new User("Updated", "Updated@email.com");
        //Set his set of polls to be one new poll and one old poll
        Poll poll3 = new Poll("Q3", Instant.now(), Instant.now().plusSeconds(3600));
        poll3.setCreator(updatedUser);
        poll1.setCreator(updatedUser);
        updatedUser.setCreatedPolls(List.of(poll1, poll3));
        //Set his set of votes to be one new vote and one old vote
        Vote vote3 = new Vote(Instant.now());
        vote3.setUser(updatedUser);
        vote1.setUser(updatedUser);
        updatedUser.setCastedVotes(List.of(vote1,vote3));

        Optional<User> updatedUserOPT = service.updateUser(createdUser.getId(), updatedUser);
        //Check that the user was updated
        assertTrue(updatedUserOPT.isPresent());
        
        updatedUser = updatedUserOPT.get();
        poll1 = updatedUser.getCreatedPolls().get(0);
        poll3 = updatedUser.getCreatedPolls().get(1);
        vote1 = updatedUser.getCastedVotes().get(0);
        vote3 = updatedUser.getCastedVotes().get(1);
        //Check that the id was not changed
        assertTrue(updatedUser.getId().equals(createdUser.getId()));
        //Retrieve the user from the database
        User retrievedUser = service.getUserById(updatedUser.getId()).get();
        //Check that the correct username and mail has been saved
        assertTrue(retrievedUser.getUsername().equals(updatedUser.getUsername()));
        assertTrue(retrievedUser.getEmail().equals(updatedUser.getEmail()));

        //Check that the correct Polls has been saved for the user
        assertTrue(retrievedUser.getCreatedPolls().get(0).equals(updatedUser.getCreatedPolls().get(0)));
        assertTrue(retrievedUser.getCreatedPolls().get(1).equals(updatedUser.getCreatedPolls().get(1)));
        assertTrue(retrievedUser.getCreatedPolls().get(0).getQuestion().equals("Q1"));
        assertTrue(retrievedUser.getCreatedPolls().get(1).getQuestion().equals("Q3"));
        assertTrue(retrievedUser.getCreatedPolls().size() == 2);
        assertTrue(updatedUser.getCreatedPolls().size() == 2);

        //Check that the correct Votes has been saved for the user
        assertTrue(retrievedUser.getCastedVotes().get(0).equals(updatedUser.getCastedVotes().get(0)));
        assertTrue(retrievedUser.getCastedVotes().get(1).equals(updatedUser.getCastedVotes().get(1)));
        assertTrue(retrievedUser.getCastedVotes().size() == 2);
        assertTrue(updatedUser.getCastedVotes().size() == 2);

        //Check that the Polls not in the updated users list was deleted
        List<Poll> retrievedPolls = pService.getAllPolls();
        assertTrue(retrievedPolls.get(0).getQuestion().equals("Q1"));
        assertTrue(retrievedPolls.get(1).getQuestion().equals("Q3"));
        assertTrue(retrievedPolls.contains(poll1));
        assertTrue(retrievedPolls.contains(poll3));
        assertTrue(retrievedPolls.size() == 2);

        //Check that the Votes not in the updated votes list was deleted
        List<Vote> retrievedVotes = vService.getAllVotes();
        assertTrue(retrievedVotes.get(0).equals(retrievedUser.getCastedVotes().get(0)));
        assertTrue(retrievedVotes.get(1).equals(retrievedUser.getCastedVotes().get(1)));
        assertTrue(retrievedVotes.contains(vote1));
        assertTrue(retrievedVotes.contains(vote3));
        assertTrue(!retrievedVotes.contains(vote2));
        assertTrue(retrievedVotes.size() == 2);
    }
}
