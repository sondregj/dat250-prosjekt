package no.hvl.dat250.polls.Services;

import java.time.Instant;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.hvl.dat250.polls.Repository.GuestUserRepository;
import no.hvl.dat250.polls.models.guestUser;

/**
 * GuestUserService
 * @author Jonas Vestbø
 */
@Service
public class GuestUserService {


    @Autowired GuestUserRepository repo;

    public Optional<guestUser> getGuestById(String id){
        return repo.findById(id);
    }

    /**
     *Retrieves the guest user with given id. 
     *If it is still valid it extends the valid time and saves it in the database
     *If the user is not still valid it returns and emptyOptional
     *@param id The id of the guest user
     *@return The guestUser with given id with extended validUntil or an empty Optioanl
     */
    public Optional<guestUser>getCheckAndExtendById(String id){
            
        if (repo.findById(id).isEmpty()){
            return Optional.empty();
        }
        guestUser retrieved = repo.findById(id).get();
        if (retrieved.getValidUntil().compareTo(Instant.now()) < 0){
            return Optional.empty();
        }

        retrieved.extendValidUntil();
        return Optional.of(repo.save(retrieved));
    }

    public guestUser registerGuestUser(){
        guestUser user = new guestUser();
        return repo.save(user);
    }
    
}
