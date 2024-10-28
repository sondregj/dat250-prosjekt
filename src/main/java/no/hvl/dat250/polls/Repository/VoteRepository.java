package no.hvl.dat250.polls.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import no.hvl.dat250.polls.models.Vote;

/**
 * VoteRepository
 */
public interface VoteRepository extends JpaRepository<Vote, Long>{
    /**
     * Add more methods here if needed using the 
     * @Query(JPAsql)
     */
}
