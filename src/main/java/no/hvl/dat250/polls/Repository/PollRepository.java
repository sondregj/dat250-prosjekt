package no.hvl.dat250.polls.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import no.hvl.dat250.polls.models.Poll;

/**
 * PollRepository
 */
public interface PollRepository extends JpaRepository<Poll, Long>{
    /**
     * Add more methods here if needed for example:
     * @Query("SELECT * WHERE ID = 1")
     * public Poll getWithId1();
     */



}
