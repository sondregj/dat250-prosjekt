package no.hvl.dat250.polls.UnitTests.Service;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import no.hvl.dat250.polls.Repository.UserRepository;
import no.hvl.dat250.polls.Services.UserService;
import no.hvl.dat250.polls.models.User;

/**
 * UserServiceUnitTest
 */

@SpringBootTest
@ActiveProfiles("test")
public class UserServiceUnitTest {

    @Autowired UserRepository repo;
    @Autowired UserService service;

    @BeforeEach
    void setUp(){
        repo.deleteAll();
    }

    @Test
    public void testSaveUser(){
        User createdUser = new User("Test", "Test@email.com");
        createdUser = service.addUser(createdUser);
        assertTrue(createdUser != null);
        assertTrue(createdUser.getId() != null);
        assertTrue(createdUser.getUsername().equals("Test"));
        assertTrue(createdUser.getEmail().equals("Test@email.com"));
    }
    
    @Test
    public void testGetUserById(){
        User createdUser = new User("Test", "Test@email.com");
        createdUser = service.addUser(createdUser);

        User retrievedUser = service.getUserById(createdUser.getId()).get();
        assertTrue(retrievedUser.equals(createdUser));
    }

    @Test
    public void testGetAllUsers(){
        User createdUser = new User("Test", "Test@email.com");
        User createdUser2 = new User("Test2", "Test2@email.com");
        createdUser = service.addUser(createdUser);
        createdUser2 = service.addUser(createdUser2);

        List<User> allUsers = service.getAllUsers();

        assertTrue(allUsers.contains(createdUser));
        assertTrue(allUsers.contains(createdUser2));
        assertTrue(allUsers.size() == 2);
    }

    @Test
    public void testDeleteUserById(){
        User createdUser = new User("Test", "Test@email.com");
        createdUser = service.addUser(createdUser);

        assertTrue(createdUser.getId() != null);

        assertTrue(service.deleteUserById(createdUser.getId()));

        assertTrue(service.getUserById(createdUser.getId()).isEmpty());
    }

    @Test
    public void testDeleteUser(){
        User createdUser = new User("Test", "Test@email.com");
        createdUser = service.addUser(createdUser);

        assertTrue(createdUser.getId() != null);

        assertTrue(service.deleteUser(createdUser));

        assertTrue(service.getUserById(createdUser.getId()).isEmpty());
    }

    @Test
    public void testUpdateUser(){
        User createdUser = new User("Test", "Test@email.com");
        createdUser = service.addUser(createdUser);

        User updatedUser = new User("Updated", "Updated@email.com");
        Optional<User> updatedUserOPT = service.updateUser(createdUser.getId(), updatedUser);

        assertTrue(updatedUserOPT.isPresent());
        
        updatedUser = updatedUserOPT.get();

        assertTrue(updatedUser.getId().equals(createdUser.getId()));

        User retrievedUser = service.getUserById(updatedUser.getId()).get();

        assertTrue(retrievedUser.getUsername().equals(updatedUser.getUsername()));
        assertTrue(retrievedUser.getEmail().equals(updatedUser.getEmail()));
    }
}
