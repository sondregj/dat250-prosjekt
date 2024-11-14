package no.hvl.dat250.polls.Services;

import java.util.List;
import java.util.Optional;

import no.hvl.dat250.polls.models.User;
import no.hvl.dat250.polls.models.Vote;
import no.hvl.dat250.polls.models.VoteOption;
import no.hvl.dat250.polls.models.guestUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import no.hvl.dat250.polls.Repository.UserRepository;
import no.hvl.dat250.polls.Repository.VoteRepository;
import no.hvl.dat250.polls.Repository.VoteOptionRepository;

/**
 * VoteService
 * @author Jonas Vestbø
 */
@Service
public class VoteService {

    @Autowired VoteRepository repo;
    @Autowired VoteOptionRepository vRepo;
    @Autowired UserRepository uRepo;

    public List<Vote> getAllVotes(){
        return repo.findAll();
    }

    /**
     *@param id The id of the user you want to retrieve 
     *@return the retrieved user or an empty optional if no user could be found
     */
    public Optional<Vote> getVoteById(Long id){
        return repo.findById(id);
    }

    /**
     *@param vote the vote object you want to save to the database
     *@return the saved vote
     */
    @Transactional
    public Vote addVote(Vote vote){
        return repo.save(vote);
    }

    /**
     *@param vote the vote you want to delete
     *@return True if the vote was deleted, false if not
     */
    @Transactional
    public boolean deleteVote(Vote vote){
        repo.delete(vote);
        return getVoteById(vote.getId()).isEmpty();
    }
    /**
     *@param id the id of the vote you want to delete
     *@return True if the vote was deleted, false if not
     */
    @Transactional
    public boolean deleteVoteById(Long id){
        Optional<Vote> retrievedVote = repo.findById(id);
        if (retrievedVote.isEmpty()){
            return false;
        }

        Vote vote = retrievedVote.get();
        
        User user = vote.getUser();
        if (user != null){
            user.getCastedVotes().remove(vote);
            uRepo.save(user);
        }
        VoteOption option = vote.getVoteOption();
        if (option != null){
            option.getVotes().remove(vote);
            vRepo.save(option);
        }

        repo.deleteById(id);
        return getVoteById(id).isEmpty();
    }
    /**
     *@param vote the vote you want to delete
     *@return True if the vote was deleted, false if not
     */
    @Transactional
    public Optional<Vote> updateVote(Long id, Vote updatedVote){
        Optional<Vote> oldVoteOpt = getVoteById(id);
        if (oldVoteOpt.isEmpty()){
            return oldVoteOpt;
        }
        Vote oldVote = oldVoteOpt.get();

        if (updatedVote.getVoteOption() != null){
            System.out.println("Voteoption was updated");
            oldVote.setVoteOption(updatedVote.getVoteOption());
        } else {
            System.out.println("VoteOption was not updated");
        }
        if (updatedVote.getPublishedAt() != null){
            oldVote.setPublishedAt(updatedVote.getPublishedAt());
        }
        System.out.println("OldVote: " + oldVote.toString());
        return Optional.of(repo.save(oldVote));
    }
    /**
     *Votes on a voteoption, if the guest user casting the vote already has
     *a vote on the poll being voted on, the vote will be updated
     *@param guest the guestUser that is performing the vote
     *@param vote the Vote that the user want to add
     *@return The created Vote or an empty Optional if something fails
     */ 
    @Transactional
    public Optional<Vote> handleGuestVote(guestUser guest, Vote vote){
        Optional<Vote> existingVote = findGuestVoteOnPoll(guest, vote);
        //Check if there exists a vote on the poll
        if (existingVote.isPresent()){
            //Update the vote
            System.out.println("Found existing vote");
            return updateVote(existingVote.get().getId(), vote);
        }
        //Create a new vote
        System.out.println("Found vote");
        vote.setGuest(guest);
        Vote createdVote  = addVote(vote);
        return Optional.of(createdVote);
    }
    
    /**
     *Votes on a voteoption, if the user casting the vote already has
     *a vote on the poll being voted on, the vote will be updated
     *@param user the User that is performing the vote
     *@param vote the Vote that the user want to add
     *@return The created Vote or an empty Optional if something fails
     */ 
    @Transactional
    public Optional<Vote> handleUserVote(User user, Vote vote){
        //Check if there exists a vote on the poll
        Optional<Vote> existingVote = findUserVoteOnPoll(user, vote);
        if (existingVote.isPresent()){
            //Update the vote
            System.out.println("Found existing vote");
            return updateVote(existingVote.get().getId(), vote);
        }
        System.out.println("Did not find existing vote"); 
        //Create a new vote
        vote.setUser(user);
        Vote createdVote  = addVote(vote);
        return Optional.of(createdVote);
    }

    /**Checks if a user has an existing vote on a poll that is found via a vote
     *@param user the user we want to find vote for
     *@param vote the vote that the user wants to cast
     *@return An existing vote if the user already had a vote on the poll, an empty optional if not
     */
        public Optional<Vote> findUserVoteOnPoll(User user, Vote vote) {
            // Ensure vote and voteOption are not null
            if (vote == null || vote.getVoteOption() == null) {
                System.out.println("Vote or voteoption is null");
                return Optional.empty();
            }

            // Retrieve the poll ID directly
            Long pollId = repo.getPollIdByVoteOptionId(vote.getVoteOption().getId());
            System.out.println(pollId);

            Optional<Vote> retrievedVote = repo.findByUserAndPollId(user.getId(), pollId);
            System.out.println(retrievedVote);
            return retrievedVote;
        }


    /**Checks if a guestuser has an existing vote on a poll that is found via a vote
     *@param guest the guestuser we want to find vote for
     *@param vote the vote that the user wants to cast
     *@return An existing vote if the user already had a vote on the poll, an empty optional if not
     */
        public Optional<Vote> findGuestVoteOnPoll(guestUser guest, Vote vote) {
            if (vote == null || vote.getVoteOption() == null) {
                System.out.println("Vote or option is null");
                return Optional.empty();
            }

            // Retrieve the Poll ID from the VoteOption
            Long pollId = repo.getPollIdByVoteOptionId(vote.getVoteOption().getId());

            // Use the repository method to find the vote
            return repo.findGuestVoteOnPoll(guest, pollId);
        }


}
    
