package no.hvl.dat250.polls.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.hvl.dat250.polls.Repository.VoteOptionRepository;
import no.hvl.dat250.polls.models.VoteOption;

/**
 * VoteOption
 * @author Jonas Vestbø
 */

@Service
public class VoteOptionService {
    
    @Autowired VoteOptionRepository repo;

    public List<VoteOption> getAllVoteOptions(){
        return repo.findAll();
    }



    
}
