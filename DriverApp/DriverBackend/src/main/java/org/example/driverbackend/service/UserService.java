package org.example.driverbackend.service;

import org.example.driverbackend.model.User;
import org.example.driverbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@Service
@CrossOrigin("http://localhost:4200")
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<User> processRegistrationAttempt(User newUser) {
        if (userRepository.checkForExistingUser(newUser.getEmail(), newUser.getUsername())) {
            if (newUser.getPassword().length() < 8) {
                System.out.println("Password needs to be at least 8 characters!");
                return new ResponseEntity<User>(new User(), HttpStatus.BAD_REQUEST);
            } else {
                System.out.println("Processing...");
                newUser.setRole("user");
                return new ResponseEntity<User>(this.userRepository.registerUser(newUser), HttpStatus.CREATED);
            }
        } else {
            System.out.println("User with email / username already exists");
            return new ResponseEntity<User>(HttpStatus.CONFLICT);
        }
    }

    public ResponseEntity<User> processLoginAttempt(User loginUser) {
        List<User> users;
        try {
            users = userRepository.getLoginUser(loginUser);

            if (users.isEmpty()) {
                System.out.println("User with email / username not found");
                return new ResponseEntity<User>((User) null, HttpStatus.NOT_FOUND);
            }
        } catch (EmptyResultDataAccessException e) {
            System.out.println("NO USER FOUND FOR " + loginUser.getUsername());
            return new ResponseEntity<User>((User) null, HttpStatus.NOT_FOUND);
        }

        if (users.get(0).checkPassword(loginUser.getPassword())) {
            System.out.println("CORRECT PASSWORD");
        } else {
            System.out.println("INCORRECT PASSWORD");
            return new ResponseEntity<User>((User) null, HttpStatus.UNAUTHORIZED);
        }
        System.out.println("Getting User from db...");
        return new ResponseEntity<User>(userRepository.getLoginUserObject(loginUser), HttpStatus.OK);
    }

    public ResponseEntity<User> setDriver(long id) {
        User updateUser = userRepository.updateUserRole(id, "driver");
        System.out.println("Role updated");
        return new ResponseEntity<User>(updateUser, HttpStatus.OK);
    }

    public ResponseEntity<User> setAdmin(Long id) {
        User adminUser = userRepository.updateUserRole(id, "admin");
        System.out.println("Role updated");
        return new ResponseEntity<User>(adminUser, HttpStatus.OK);
    }
}
