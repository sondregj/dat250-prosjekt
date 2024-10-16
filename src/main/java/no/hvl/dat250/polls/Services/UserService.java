package no.hvl.dat250.polls.Services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import no.hvl.dat250.polls.Repository.UserRepository;
import no.hvl.dat250.polls.models.User;

/**
 * UserService
 * @author Jonas Vestbø
 */
@Service
public class UserService {

    @Autowired UserRepository repo;
    
    /**
     *@return A list containing all Users
     */
    public List<User> getAllUsers(){
        return repo.findAll();
    }

    /**
     *@param id The id of the user you want to retrieve 
     *@return An optional containing the user with the given id or an empty optional if it does 
     * not exist
     */
    public Optional<User> getUserById(Long id){
        return repo.findById(id);
    }

    /**
     * @param id The id of the user you want to delete 
     * @return True if the user was deleted, False if not
     */
    @Transactional
    public boolean deleteUserById(Long id){
        // TODO add a feature that when a user is deleted his votes are deleted as well?
        repo.deleteById(id);
        return repo.findById(id).isEmpty();
    }

    /**
     * @param user The user you want to delete 
     * @return True if the user was deleted, False if not
     */
    
    @Transactional
    public boolean deleteUser(User user){
        // TODO add a feature that when a user is deleted his votes are deleted as well?
        repo.delete(user);
        return repo.findById(user.getId()).isEmpty();
    }

    /**
     *@param id The id of the user you want to update 
     *@param updatedUser the User object you want to update to
     *@return The updateduser or an empty optional if the user could not be updated
     */

    @Transactional
    public Optional<User> updateUser(Long id, User updatedUser){
        Optional<User> oldUserOpt = getUserById(id);
        if (oldUserOpt.isEmpty()){
            return Optional.empty();
        }
        User oldUser = oldUserOpt.get();
        
        oldUser.setEmail(updatedUser.getEmail());
        oldUser.setUsername(updatedUser.getUsername());
        // TODO add possibility to add votes and createdpolls as well
        // TODO if so also add so that the removed votes and created polls are deleted?

        return Optional.of(repo.save(oldUser));
    }

    /**
     * @param user the User you want to save to the database 
     * @return the saved User or throws an error if the User could not be saved
     */

    @Transactional
    public User addUser(User user){
        return repo.save(user);
    }
}
