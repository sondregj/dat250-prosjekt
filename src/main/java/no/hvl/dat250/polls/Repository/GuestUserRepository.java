package no.hvl.dat250.polls.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import no.hvl.dat250.polls.models.guestUser;

/**
 * GuestUserRepository
 */
public interface GuestUserRepository extends JpaRepository<guestUser, String>{
}
