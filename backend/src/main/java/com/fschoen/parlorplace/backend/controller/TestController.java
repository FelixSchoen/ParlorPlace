package com.fschoen.parlorplace.backend.controller;

import com.fschoen.parlorplace.backend.utility.Messages;
import com.fschoen.parlorplace.backend.entity.persistance.User;
import com.fschoen.parlorplace.backend.exceptions.DataConflictException;
import com.fschoen.parlorplace.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/user")
@RestController
public class TestController {

    private static final String BASE_URL = "/user";
    private final UserRepository userRepository;

    @Autowired
    public TestController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/add")
    public String addUser(@RequestParam String username, @RequestParam String email) throws RuntimeException {
        User user = new User();
        user.setUsername(username);
        user.setNickname(username);
        user.setEmail(email);

        if (userRepository.findOneByUsername(username).isPresent() || userRepository.findOneByEmail(email).isPresent()) {
            throw new DataConflictException(Messages.getExceptionExplanationMessage("user.name.exists"));
        }

        userRepository.save(user);
        return "Added user";
    }

    @GetMapping("/list")
    public Iterable<User> getUsers() {
        return userRepository.findAll();
    }

}
