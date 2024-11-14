package no.hvl.dat250.polls.UnitTests.Service;

import no.hvl.dat250.polls.Repository.GuestUserRepository;
import no.hvl.dat250.polls.Repository.PollRepository;
import no.hvl.dat250.polls.Repository.UserRepository;
import no.hvl.dat250.polls.Repository.VoteRepository;
import no.hvl.dat250.polls.Services.GuestUserService;
import no.hvl.dat250.polls.Services.PollService;
import no.hvl.dat250.polls.Services.UserService;
import no.hvl.dat250.polls.Services.VoteService;
import no.hvl.dat250.polls.dto.UserCreationDTO;
import no.hvl.dat250.polls.models.Poll;
import no.hvl.dat250.polls.models.User;
import no.hvl.dat250.polls.models.Vote;
import no.hvl.dat250.polls.models.VoteOption;
import no.hvl.dat250.polls.models.guestUser;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

/**
 * VoteServiceUnitTest
 */
@SpringBootTest
@ActiveProfiles("test")
public class VoteServiceUnitTest {

    @Autowired VoteRepository repo;
    @Autowired VoteService service;
    @Autowired PollService pollService;
    @Autowired PollRepository polleRepository;
    @Autowired UserService userService;
    @Autowired UserRepository userRepo;
    @Autowired GuestUserService guestUserService;
    @Autowired GuestUserRepository guestRepo;

    @PersistenceContext
    private EntityManager manager;

    // @AfterEach
    // void tearDown() {
    //     manager.createNativeQuery("TRUNCATE TABLE vote CASCADE").executeUpdate();
    //     manager.createNativeQuery("TRUNCATE TABLE poll CASCADE").executeUpdate();
    //     manager.createNativeQuery("TRUNCATE TABLE user CASCADE").executeUpdate();
    //     manager.createNativeQuery("TRUNCATE TABLE guest_user CASCADE").executeUpdate();
    // }



    @BeforeEach
    @Transactional
    void setUp(){
        repo.deleteAll();
        polleRepository.deleteAll();
        userRepo.deleteAll();
        guestRepo.deleteAll();
        manager.flush();
    }

    @Test
    @Transactional
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
    @Transactional
    void deleteVoteByIdTest(){
        Vote vote = new Vote(Instant.now());
        vote = service.addVote(vote);
        
        assertTrue(service.deleteVoteById(vote.getId()));

        assertTrue(service.getVoteById(vote.getId()).isEmpty());
    }

    @Test
    @Transactional
    void deleteVoteTest(){
        Vote vote = new Vote(Instant.now());
        vote = service.addVote(vote);
        
        assertTrue(service.deleteVote(vote));

        assertTrue(service.getVoteById(vote.getId()).isEmpty());
    }

    @Test
    @Transactional
    void updatedVoteTest(){
        //Add a user
        UserCreationDTO userDTO = new UserCreationDTO("Test1", "Test", "Test");
        User user = userService.addUser(userDTO);

        //Add a poll
        Poll poll = new Poll();
        poll.setQuestion("Hva smaker best?");
        VoteOption option1 = new VoteOption("A", 0);
        VoteOption option2 = new VoteOption("B", 1);
        VoteOption option3 = new VoteOption("C", 2);
        option1.setPoll(poll);
        option2.setPoll(poll);
        option3.setPoll(poll);
        poll.setVoteOptions(List.of(option1,option2,option3));
        poll.setCreator(user);
        poll = pollService.addPoll(poll);

        manager.flush();
        manager.clear();

        //Now vote on the poll  
        Vote vote = new Vote(Instant.now());
        vote.setUser(user);
        vote.setVoteOption(option1);
        vote = service.addVote(vote);
        System.out.println(vote);
            
        // manager.flush();
        // manager.clear();
        
        Vote newVote = new Vote(Instant.now());
        newVote.setUser(user);
        newVote.setVoteOption(option2);
        Optional<Vote> updatedVoteOPT = service.updateVote(vote.getId(), newVote);
        System.out.println(updatedVoteOPT.get());

        // manager.flush();
        // manager.clear();

        assertTrue(updatedVoteOPT.isPresent());
        assertTrue(updatedVoteOPT.get().getId().equals(vote.getId()));
        assertTrue(updatedVoteOPT.get().getUser().equals(newVote.getUser()));
        assertTrue(updatedVoteOPT.get().getVoteOption().equals(newVote.getVoteOption()));

        List<Vote> allVotes = service.getAllVotes();
        System.out.println(allVotes);
        assertTrue(allVotes.size() == 1);
        assertTrue(allVotes.getFirst().equals(updatedVoteOPT.get()));
    }
        
    @Test
    @Transactional
    void testFindVoteForGuestUser(){
        //Add a user
        //
        UserCreationDTO userDTO = new UserCreationDTO("Test3", "Test", "Test");
        User user = userService.addUser(userDTO);
        //Add a guest user
        guestUser guestUser = guestUserService.registerGuestUser();

        //Add a poll
        Poll poll = new Poll();
        poll.setQuestion("Hva smaker best?");
        VoteOption option1 = new VoteOption("A", 0);
        VoteOption option2 = new VoteOption("B", 1);
        VoteOption option3 = new VoteOption("C", 2);
        option1.setPoll(poll);
        option2.setPoll(poll);
        option3.setPoll(poll);
        poll.setVoteOptions(List.of(option1,option2,option3));
        poll.setCreator(user);
        poll = pollService.addPoll(poll);

        manager.flush();
        manager.clear();

        //Now vote on the poll  
        Vote vote = new Vote(Instant.now());
        vote.setGuest(guestUser);
        vote.setVoteOption(option1);
        vote = service.addVote(vote);
            
        manager.flush();
        manager.clear();
        

        //Check that the service method works
        //Create a new unsaved vote on the same poll 
        Vote newVote = new Vote(Instant.now());
        newVote.setGuest(guestUser);
        newVote.setVoteOption(option2);

        //Check if the method found the existing vote on the poll
        Optional<Vote> existingVote = service.findGuestVoteOnPoll(guestUser, newVote);
        assertTrue(existingVote.get().equals(vote));
    }

    @Test
    @Transactional
    void testFindVoteForUser(){
        //Add a user
        UserCreationDTO userDTO = new UserCreationDTO("Test", "Test", "Test");
        User user = userService.addUser(userDTO);

        //Add a poll
        Poll poll = new Poll();
        poll.setQuestion("Hva smaker best?");
        VoteOption option1 = new VoteOption("A", 0);
        VoteOption option2 = new VoteOption("B", 1);
        VoteOption option3 = new VoteOption("C", 2);
        option1.setPoll(poll);
        option2.setPoll(poll);
        option3.setPoll(poll);
        poll.setVoteOptions(List.of(option1,option2,option3));
        poll.setCreator(user);
        poll = pollService.addPoll(poll);

        manager.flush();
        manager.clear();

        //Now vote on the poll  
        Vote vote = new Vote(Instant.now());
        vote.setUser(user);
        vote.setVoteOption(option1);
        vote = service.addVote(vote);
            
        manager.flush();
        manager.clear();
        

        //Check that the service method works
        //Create a new unsaved vote on the same poll 
        Vote newVote = new Vote(Instant.now());
        newVote.setVoteOption(option2);

        //Check if the method found the existing vote on the poll
        Optional<Vote> existingVote = service.findUserVoteOnPoll(user, newVote);
        assertTrue(existingVote.get().equals(vote));
    }

    @Test
    @Transactional
    void testHandleGuestUserVote(){
        //Add a user
        UserCreationDTO userDTO = new UserCreationDTO("Test", "Test", "Test");
        User user = userService.addUser(userDTO);
        //Add a guest user
        guestUser guest = guestUserService.registerGuestUser();
        //Add a poll
        Poll poll = new Poll();
        poll.setQuestion("Hva smaker best?");
        VoteOption option1 = new VoteOption("A", 0);
        VoteOption option2 = new VoteOption("B", 1);
        VoteOption option3 = new VoteOption("C", 2);
        option1.setPoll(poll);
        option2.setPoll(poll);
        option3.setPoll(poll);
        poll.setVoteOptions(List.of(option1,option2,option3));
        poll.setCreator(user);
        poll = pollService.addPoll(poll);

        manager.flush();
        manager.clear();

        //Now vote on the poll  
        Vote vote = new Vote(Instant.now());
        vote.setGuest(guest);
        vote.setVoteOption(option1);
        vote = service.handleGuestVote(guest, vote).get();
            
        manager.flush();
        manager.clear();

        guestUser updatedUser = guestUserService.getGuestById(guest.getGuestId()).get();
        List<Vote> allVotes = service.getAllVotes();
        List<Vote> allVotesOnOption1 = pollService.getPollById(poll.getId()).get()
            .getVoteOptions().getFirst().getVotes();

        assertTrue(updatedUser.getVotes().size() == 1);
        assertTrue(updatedUser.getVotes().contains(vote));

        assertTrue(allVotes.size() == 1);
        assertTrue(allVotes.contains(vote));

        assertTrue(allVotesOnOption1.size() == 1);
        assertTrue(allVotesOnOption1.contains(vote));
            
        //Now vote on the poll  
        Vote newVote = new Vote(Instant.now());
        newVote.setGuest(guest);
        newVote.setVoteOption(option2);
        newVote = service.handleGuestVote(guest, newVote).get();
            
        manager.flush();
        manager.clear();

         updatedUser = guestUserService.getGuestById(guest.getGuestId()).get();
         allVotes = service.getAllVotes();
         allVotesOnOption1 = pollService.getPollById(poll.getId()).get()
            .getVoteOptions().get(1).getVotes();

        assertTrue(updatedUser.getVotes().size() == 1);
        assertTrue(updatedUser.getVotes().contains(newVote));

        assertTrue(allVotes.size() == 1);
        assertTrue(allVotes.contains(newVote));

        assertTrue(allVotesOnOption1.size() == 1);
        assertTrue(allVotesOnOption1.contains(newVote));
    }
    
    @Test
    @Transactional
    void testHandleUserVote(){
        //Add a user
        UserCreationDTO userDTO = new UserCreationDTO("Test", "Test", "Test");
        User user = userService.addUser(userDTO);

        //Add a poll
        Poll poll = new Poll();
        poll.setQuestion("Hva smaker best?");
        VoteOption option1 = new VoteOption("A", 0);
        VoteOption option2 = new VoteOption("B", 1);
        VoteOption option3 = new VoteOption("C", 2);
        option1.setPoll(poll);
        option2.setPoll(poll);
        option3.setPoll(poll);
        poll.setVoteOptions(List.of(option1,option2,option3));
        poll.setCreator(user);
        poll = pollService.addPoll(poll);

        manager.flush();
        manager.clear();

        //Now vote on the poll  
        Vote vote = new Vote(Instant.now());
        vote.setUser(user);
        vote.setVoteOption(option1);
        vote = service.handleUserVote(user, vote).get();
            
        manager.flush();
        manager.clear();

        User updatedUser = userService.getUserById(user.getId()).get();
        List<Vote> allVotes = service.getAllVotes();
        List<Vote> allVotesOnOption1 = pollService.getPollById(poll.getId()).get()
            .getVoteOptions().getFirst().getVotes();
        
        assertTrue(updatedUser.getCastedVotes().size() == 1);
        assertTrue(updatedUser.getCastedVotes().contains(vote));

        assertTrue(allVotes.size() == 1);
        assertTrue(allVotes.contains(vote));

        assertTrue(allVotesOnOption1.size() == 1);
        assertTrue(allVotesOnOption1.contains(vote));
            
        //Now vote on the poll  
        Vote newVote = new Vote(Instant.now());
        newVote.setUser(user);
        newVote.setVoteOption(option2);
        newVote = service.handleUserVote(user, newVote).get();
            
        manager.flush();
        manager.clear();

         updatedUser = userService.getUserById(user.getId()).get();
         allVotes = service.getAllVotes();
         allVotesOnOption1 = pollService.getPollById(poll.getId()).get()
            .getVoteOptions().get(1).getVotes();

        assertTrue(updatedUser.getCastedVotes().size() == 1);
        assertTrue(updatedUser.getCastedVotes().contains(newVote));

        assertTrue(allVotes.size() == 1);
        assertTrue(allVotes.contains(newVote));

        assertTrue(allVotesOnOption1.size() == 1);
        assertTrue(allVotesOnOption1.contains(newVote));
            
        //Now vote on the poll  
        newVote = new Vote(Instant.now());
        newVote.setUser(user);
        newVote.setVoteOption(option3);
        newVote = service.handleUserVote(user, newVote).get();
            
        manager.flush();
        manager.clear();

         updatedUser = userService.getUserById(user.getId()).get();
         allVotes = service.getAllVotes();
         allVotesOnOption1 = pollService.getPollById(poll.getId()).get()
            .getVoteOptions().get(2).getVotes();

        assertTrue(updatedUser.getCastedVotes().size() == 1);
        assertTrue(updatedUser.getCastedVotes().contains(newVote));

        assertTrue(allVotes.size() == 1);
        assertTrue(allVotes.contains(newVote));

        assertTrue(allVotesOnOption1.size() == 1);
        assertTrue(allVotesOnOption1.contains(newVote));

        //Add a guest user
        guestUser guest = guestUserService.registerGuestUser();
              
        manager.flush();
        manager.clear();
        
        //Now vote on the poll  
        Vote guestvote = new Vote(Instant.now());
        guestvote.setGuest(guest);
        guestvote.setVoteOption(option1);
        System.out.println("Guest vote"+ guestvote);
        guestvote = service.handleGuestVote(guest, guestvote).get();
        System.out.println("Guest vote"+ guestvote);
            

        //Now vote on the poll  
        newVote = new Vote(Instant.now());
        newVote.setUser(user);
        newVote.setVoteOption(option1);
        newVote = service.handleUserVote(user, newVote).get();
            
        manager.flush();
        manager.clear();

         updatedUser = userService.getUserById(user.getId()).get();
         allVotes = service.getAllVotes();
         allVotesOnOption1 = pollService.getPollById(poll.getId()).get()
            .getVoteOptions().get(0).getVotes();

        assertTrue(updatedUser.getCastedVotes().size() == 1);
        assertTrue(updatedUser.getCastedVotes().contains(newVote));

        assertTrue(allVotes.size() == 2);
        assertTrue(allVotes.contains(newVote));

        assertTrue(allVotesOnOption1.size() == 2);
        assertTrue(allVotesOnOption1.contains(newVote));

    }
    
}
