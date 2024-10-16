package no.hvl.dat250.polls.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import no.hvl.dat250.polls.models.VoteOption;

/**
 * VoteOptionRepository
 */
public interface VoteOptionRepository extends JpaRepository<VoteOption, Long> {
    /**
     * Add more methods here if needed for example:
     * @Query("SELECT * WHERE ID = 1")
     * public User getWithId1();
     */
    
}
