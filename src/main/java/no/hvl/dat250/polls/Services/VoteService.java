package no.hvl.dat250.polls.Services;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import no.hvl.dat250.polls.models.Poll;
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
        Optional<VoteOption> vo = vRepo.findById(vote.getVoteOption().getId());
        if (vo.isEmpty()){
            return null;
        }
        Poll poll = vo.get().getPoll();
        if (poll.getValidUntil().compareTo(Instant.now()) > 0){
            return null;
        }

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

    public Optional<Vote> findUserVoteOnPoll(User user, Vote vote){
        VoteOption retrievdVO = vRepo.getReferenceById(vote.getVoteOption().getId());
        return retrievdVO.getPoll().getVoteOptions().stream()
            .flatMap(vo -> vo.getVotes().stream())
            .filter(v -> v.getUser().equals(user))
            .findAny();
    }

    public Optional<Vote> findGuestVoteOnPoll(guestUser guest, Vote vote){
        VoteOption retrievdVO = vRepo.getReferenceById(vote.getVoteOption().getId());
        return retrievdVO.getPoll().getVoteOptions().stream()
            .flatMap(vo -> vo.getVotes().stream())
            .filter(v -> v.getGuest().equals(guest))
            .findAny();
    }

}
    
