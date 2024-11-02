package no.hvl.dat250.polls.Services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.hvl.dat250.polls.Repository.GuestUserRepository;
import no.hvl.dat250.polls.models.guestUser;

/**
 * GuestUserService
 */
@Service
public class GuestUserService {


    @Autowired GuestUserRepository repo;

    public Optional<guestUser> getGuestById(String id){
        return repo.findById(id);
    }
    
}
