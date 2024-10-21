package no.hvl.dat250.polls.Services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import no.hvl.dat250.polls.Repository.PollRepository;
import no.hvl.dat250.polls.Repository.UserRepository;
import no.hvl.dat250.polls.models.Poll;
import no.hvl.dat250.polls.models.User;
import no.hvl.dat250.polls.models.VoteOption;

/**
 * PollService
 * @author Jonas Vestbø
 */
@Service
public class PollService {

   @Autowired
   private PollRepository repo;
   @Autowired UserRepository uRepo;

   /**
    * @param id The id of the Poll you want to retrieve
    * @return The poll with the given id as an optional or an empty Optional if no poll exists
    */
   public Optional<Poll> getPollById(Long id){
       return repo.findById(id);
   }

   /**
    * @return Every poll in the database or an empty list if there are none
    */
   public List<Poll> getAllPolls(){
       return repo.findAll();
   }

   /**
    * @param id The id of the poll you want to update
    * @param updatedPoll the poll object you want to update to
    * @return the updated Poll or an empty Optional if the Poll could not be updated
    */

   @Transactional
   public Optional<Poll> updatePoll(Long id, Poll updatedPoll){
       Optional<Poll> retrievedPollOpt = repo.findById(id);

       if (retrievedPollOpt.isEmpty()){
           return Optional.empty();
       }

       Poll retrievedPoll = retrievedPollOpt.get();

       retrievedPoll.setPublishedAt(updatedPoll.getPublishedAt());
       retrievedPoll.setValidUntil(updatedPoll.getValidUntil());

       //Find all the options that should be removed
       List<VoteOption> removedVoteOptions = retrievedPoll.getVoteOptions().stream()
           .filter(p -> !updatedPoll.getVoteOptions().contains(p))
           .toList();

       //Remove them
       retrievedPoll.getVoteOptions().removeAll(removedVoteOptions);

       //Add all noexisting values
        updatedPoll.getVoteOptions().forEach(p -> {
            if (!retrievedPoll.getVoteOptions().contains(p)) {
                p.setPoll(retrievedPoll);
                retrievedPoll.getVoteOptions().add(p);
            }
        });

       return Optional.of(repo.save(retrievedPoll));
   }

   /**
    *@param poll the poll that you want to delete
    *@return True if the poll was deleted, false if not
    */
   @Transactional
   public boolean deletePoll(Poll poll){
       repo.delete(poll);
       return repo.findById(poll.getId()).isEmpty();
   }
   /**
    *@param id the id of the poll that you want to delete
    *@return True if the poll was deleted, false if not
    */
   @Transactional
   public boolean deletePollById(Long id){
       Optional<Poll> retrievedPoll = repo.findById(id);
       if (retrievedPoll.isEmpty()){
           return false;
       }
       Poll poll = retrievedPoll.get();
       User user = poll.getCreator();
       if (user != null){
           user.getCreatedPolls().remove(poll);
           uRepo.save(user);
       }
       repo.deleteById(id);
       return repo.findById(id).isEmpty();
   }
   /**
    * @param poll the poll that you want to add to the database
    * @returns the added poll or throws an exception if it was
    * not successfull
    */
   @Transactional
   public Poll addPoll(Poll poll){
       if (poll.getVoteOptions() != null && !poll.getVoteOptions().isEmpty()){
           poll.getVoteOptions().forEach(vo -> {
               vo.setPoll(poll);
           });
       }
       Poll savedPoll = repo.save(poll);
       return savedPoll;
   }
}
