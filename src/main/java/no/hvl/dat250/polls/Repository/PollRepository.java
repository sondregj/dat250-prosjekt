package no.hvl.dat250.polls.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import no.hvl.dat250.polls.models.Poll;
import no.hvl.dat250.polls.models.User;

/**
 * PollRepository
 */
public interface PollRepository extends JpaRepository<Poll, Long>{
    /**
     * Add more methods here if needed using the 
     * @Query(JPAsql)
     */
    @Query("SELECT p FROM Poll p WHERE p.creator= :user")
    List<Poll> findPollsByCreator(@Param("user") User user);

}
