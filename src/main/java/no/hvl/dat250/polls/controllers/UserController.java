package no.hvl.dat250.polls.controllers;

import java.util.List;
import java.util.Optional;
import no.hvl.dat250.polls.Services.UserService;
import no.hvl.dat250.polls.dto.UserCreationDTO;
import no.hvl.dat250.polls.models.User;
import no.hvl.dat250.polls.Error.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * UserController
 * @author Jonas Vestbø
 */

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UserService service;

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable("id") Long id) {
        Optional<User> retrievedUser = service.getUserById(id);
        if (retrievedUser.isEmpty()) {
            return new ResponseEntity<>(CommonErrors.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(retrievedUser.get(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<User> addUser(@RequestBody UserCreationDTO user) {
        User savedUser = service.addUser(user);
        return new ResponseEntity<>(savedUser, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable("id") Long id) {
        Optional<User> retrievedUser = service.getUserById(id);
        if (retrievedUser.isEmpty()) {
            return new ResponseEntity<>(
                    CommonErrors.USER_NOT_FOUND,
                    HttpStatus.NOT_FOUND
                    );
        }
        if (!service.deleteUserById(id)) {
            return new ResponseEntity<>(
                    CommonErrors.NOT_DELETED,
                    HttpStatus.INTERNAL_SERVER_ERROR
                    );
        }
        return new ResponseEntity<>(retrievedUser.get(), HttpStatus.OK);
    }

    // Unnesscesary
    // @DeleteMapping
    // public ResponseEntity<User> deleteUserById(@RequestBody User user){
    //     Optional<User> retrievedUser = service.getUserById(user.getId());
    //     if (retrievedUser.isEmpty()){
    //         return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    //     }
    //     if (!service.deleteUser(user)){
    //         return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    //     }
    //     return new ResponseEntity<>(retrievedUser.get(), HttpStatus.OK);
    // }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(
        @PathVariable("id") Long id,
        @RequestBody User updUser
    ) {
        Optional<User> updatedUser = service.updateUser(id, updUser);
        if (updatedUser.isEmpty()) {
            return new ResponseEntity<>(
                    CommonErrors.USER_NOT_FOUND,
                    HttpStatus.NOT_FOUND
                    );
        }
        return new ResponseEntity<>(updatedUser.get(), HttpStatus.OK);
    }
    // @PostMapping("/users")
    // public ResponseEntity<User> createUser(@RequestBody User user) {
    //     userService.addUser(UserCreationDTO(user));

    //     return ResponseEntity.ok(user);
    // }
    //
}
