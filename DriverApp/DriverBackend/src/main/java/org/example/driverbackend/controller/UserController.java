package org.example.driverbackend.controller;

import org.example.driverbackend.model.User;
import org.example.driverbackend.repository.UserRepository;
import org.example.driverbackend.service.UserService;
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

    @PutMapping("/setDriver/{id}")
    public ResponseEntity<User> setDriver(@PathVariable Long id) {
        return userService.setDriver(id);
    }
}
