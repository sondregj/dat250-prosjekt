package no.hvl.dat250.polls.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import no.hvl.dat250.polls.models.VoteOption;

/**
 * VoteOptionRepository
 */
public interface VoteOptionRepository extends JpaRepository<VoteOption, Long> {
    /**
     * Add more methods here if needed using the 
     * @Query(JPAsql)
     */
    
}
