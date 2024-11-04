package no.hvl.dat250.polls.UnitTests.Service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import no.hvl.dat250.polls.Repository.GuestUserRepository;
import no.hvl.dat250.polls.Services.GuestUserService;
import no.hvl.dat250.polls.models.guestUser;

/**
 * GuestUserServiceUnitTest
 */
@SpringBootTest
@ActiveProfiles("test")
public class GuestUserServiceUnitTest {


    @Autowired GuestUserService service;
    @Autowired GuestUserRepository repo;

    @BeforeEach
    public void cleanUp(){
        repo.deleteAll();
    }

    @Test
    public void testRegisterGuestUser(){
        guestUser newUser1 = service.registerGuestUser();
        guestUser newUser2 = service.registerGuestUser();
        assertFalse(newUser1.equals(newUser2));
        assertFalse(newUser1.getGuestId().equals(newUser2.getGuestId()));
        List<guestUser> allUsers = repo.findAll();
        assertTrue(allUsers.contains(newUser1));
        assertTrue(allUsers.contains(newUser2));
        assertTrue(allUsers.size() == 2);
    }

    @Test
    public void testGetCheckAndUpdate(){
        guestUser newUser1 = service.registerGuestUser();

        Optional<guestUser> newUser1UpdatedOPT = service.getCheckAndExtendById(newUser1.getGuestId());
        assertTrue(newUser1UpdatedOPT.isPresent());
        guestUser newUser1Updated = newUser1UpdatedOPT.get();
        assertTrue(newUser1.getValidUntil().compareTo(newUser1Updated.getValidUntil()) < 0 );
    }
    
}
