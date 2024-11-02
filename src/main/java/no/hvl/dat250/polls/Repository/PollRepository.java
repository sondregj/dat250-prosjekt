package no.hvl.dat250.polls.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import no.hvl.dat250.polls.models.Poll;
import no.hvl.dat250.polls.models.VoteOption;

/**
 * PollRepository
 */
public interface PollRepository extends JpaRepository<Poll, Long>{
    /**
     * Add more methods here if needed using the 
     * @Query(JPAsql)
     */
}
