package org.example.userbackend.controller;

import org.example.userbackend.model.User;
import org.example.userbackend.repository.UserRepository;
import org.example.userbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:4200")
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/getAll")
    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User newUser) {
        System.out.println("Processing registration...");
        return userService.processRegistrationAttempt(newUser);
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody User loginUser) {
        System.out.println("Processing login...");
        return userService.processLoginAttempt(loginUser);
    }

    @PutMapping("/setAdmin/{id}")
    public ResponseEntity<User> setAdmin(@PathVariable Long id) {
        return userService.setAdmin(id);
    }

    @PutMapping("/setUser/{id}")
    public ResponseEntity<User> setUser(@PathVariable Long id) {
        return userService.setUser(id);
    }
}
