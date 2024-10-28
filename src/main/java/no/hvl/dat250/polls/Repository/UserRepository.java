package no.hvl.dat250.polls.Repository;

import no.hvl.dat250.polls.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * UserRepository
 */
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Add more methods here if needed for example:
     * @Query("SELECT * WHERE ID = 1")
     * public User getWithId1();
     */

    @Query("SELECT u FROM User u WHERE u.username = :username")
    User findByUsername(@Param("username") String username);
}
