package no.hvl.dat250.polls.controllers;

import no.hvl.dat250.polls.Services.UserService;
import no.hvl.dat250.polls.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:5174", allowedHeaders = "*", methods = RequestMethod.POST)
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody User user) {


        userService.addUser(user);

        return ResponseEntity.ok(user);
    }


}
