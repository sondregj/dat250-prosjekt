package no.hvl.dat250.polls.Services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import no.hvl.dat250.polls.Repository.PollRepository;
import no.hvl.dat250.polls.Repository.VoteOptionRepository;
import no.hvl.dat250.polls.Repository.VoteRepository;
import no.hvl.dat250.polls.models.Poll;
import no.hvl.dat250.polls.models.Vote;
import no.hvl.dat250.polls.models.VoteOption;

/**
 * VoteOption
 * @author Jonas Vestbø
 */

@Service
public class VoteOptionService {
    
    @Autowired VoteOptionRepository repo;
    @Autowired VoteRepository voteRepo;
    @Autowired PollRepository pollRepo;

    /**
     *@param id the id of the voteoption you want to retrieve 
     *@return the voteoption with the id or an empty optional if a user with id does not exist
     */
    public Optional<VoteOption> getVoteOptionById(Long id){
        return repo.findById(id);
    }

    /**
     * @return a list of all voteoptions
     */
    public List<VoteOption> getAllVoteOptions(){
        return repo.findAll();
    }

    /**
     *@param voteOption the voteoption you want to save to the database
     *@return the saved voteoption
     */
    @Transactional
    public VoteOption addVoteOption(VoteOption voteOption){
        return repo.save(voteOption);
    }

    /**
     *@param id the id of the voteoption you want to delete 
     *@return True if the voteoption is deleted, false if not
     */
    @Transactional
    public boolean deleteVoteOptionById(Long id) {
        // Find the VoteOption by ID
        Optional<VoteOption> voteOptionOpt = repo.findById(id);
        if (voteOptionOpt.isPresent()) {
            VoteOption voteOption = voteOptionOpt.get();

            // If there's an associated Poll, you might want to remove the VoteOption from the Poll's list
            Poll poll = voteOption.getPoll();
            if (poll != null) {
                poll.getVoteOptionMutable().remove(voteOption);
                // Optionally save the Poll if you want to ensure that the changes are persisted
                pollRepo.save(poll);
            }

            // Remove associated votes if needed
            List<Vote> votes = voteOption.getVotes();
            if (votes != null) {
                for (Vote vote : votes) {
                    // This will trigger cascade deletion if properly set up
                    voteRepo.delete(vote);
                }
            }

            // Delete the VoteOption itself
            repo.deleteById(id);

            // Verify that the VoteOption was indeed deleted
            return repo.findById(id).isEmpty();
        }
        return false; // Return false if the VoteOption was not found
    }

    /**
     *@param voteOption the voteoption you want to delete 
     *@return True if the voteoption is deleted, false if not
     */
    @Transactional
    public boolean deleteVoteOption(VoteOption voteOption){
        repo.delete(voteOption);
        return repo.findById(voteOption.getId()).isEmpty();
    }

    /**
     *@param id the id of the voteoption you want to update 
     *@voteOption the voteOption object you want to update to
     *@return the updated voteoption or an empty optional if not
     */
    @Transactional
    public Optional<VoteOption> updateVoteOption(Long id, VoteOption updatedVoteOption){
        Optional<VoteOption> voteOptionOPT = getVoteOptionById(id);
        if (voteOptionOPT.isEmpty()){
            return Optional.empty();
        }
        VoteOption voteOption = voteOptionOPT.get();

        voteOption.setCaption(updatedVoteOption.getCaption());
        voteOption.setPresentationOrder(updatedVoteOption.getPresentationOrder());

        if (updatedVoteOption.getPoll() != null){
            voteOption.setPoll(updatedVoteOption.getPoll());
        }

        return Optional.of(repo.save(voteOption));
    }
}
