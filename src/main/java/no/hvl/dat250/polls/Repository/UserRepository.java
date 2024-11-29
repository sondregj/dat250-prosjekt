package no.hvl.dat250.polls.Repository;

import no.hvl.dat250.polls.models.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * UserRepository
 */
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Add more methods here if needed using the 
     * @Query(JPAsql)
     */

    @Query("SELECT u FROM User u WHERE u.username = :username")
    Optional<User> findByUsername(@Param("username") String username);
}
