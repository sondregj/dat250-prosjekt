package no.hvl.dat250.polls.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import no.hvl.dat250.polls.models.Vote;
import no.hvl.dat250.polls.models.guestUser;
import no.hvl.dat250.polls.models.User;

/**
 * VoteRepository
 */
public interface VoteRepository extends JpaRepository<Vote, Long>{
    /**
     * Add more methods here if needed using the 
     * @Query(JPAsql)
     */
    @Query("SELECT v FROM Vote v WHERE v.user.id = :userId AND v.voteOption.poll.id = :pollId")
    Optional<Vote> findByUserAndPollId(@Param("userId") Long userId, @Param("pollId") Long pollId);
        
    @Query("SELECT vo.poll.id FROM VoteOption vo WHERE vo.id = :voteOptionId")
    Long getPollIdByVoteOptionId(@Param("voteOptionId") Long voteOptionId);

    @Query("SELECT v FROM Vote v WHERE v.guest = :guest AND v.voteOption.poll.id = :pollId")
    Optional<Vote> findGuestVoteOnPoll(@Param("guest") guestUser guest, @Param("pollId") Long pollId);

    @Query("SELECT v FROM Vote v WHERE v.user = :user")
    List<Vote> findVotesByCaster(@Param("user") User user);

}
