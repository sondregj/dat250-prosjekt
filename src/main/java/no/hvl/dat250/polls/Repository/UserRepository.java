package no.hvl.dat250.polls.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import no.hvl.dat250.polls.models.User;

/**
 * UserRepository
 */
public interface UserRepository extends JpaRepository<User, Long>{

    /**
     * Add more methods here if needed using the 
     * @Query(JPAsql)
     */
    
}
