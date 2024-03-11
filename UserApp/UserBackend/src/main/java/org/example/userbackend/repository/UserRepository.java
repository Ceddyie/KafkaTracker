package org.example.userbackend.repository;

import org.example.userbackend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@Repository
@CrossOrigin("http://localhost:4200")
public class UserRepository {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    public List<User> getAllUsers() {
        return jdbcTemplate.query("SELECT * FROM user", new BeanPropertyRowMapper<>(User.class));
    }

    public boolean checkForExistingUser(String email, String username) {
        System.out.println("Checking db for user");

        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("email", email);
        namedParameters.addValue("username", username);

        List<User> users = jdbcTemplate.query("SELECT * FROM user WHERE email = :email OR username = :username", namedParameters, new BeanPropertyRowMapper<>(User.class));

        if (users.isEmpty()) {
            System.out.println("Username and email not in use");
            return true;
        } else {
            System.out.println("Username or email already in use");
            return false;
        }
    }

    public User registerUser(User newUser) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("username", newUser.getUsername());
        namedParameters.addValue("email", newUser.getEmail());
        namedParameters.addValue("password", passwordEncoder.encode(newUser.getPassword()));
        namedParameters.addValue("firstName", newUser.getFirstName());
        namedParameters.addValue("lastName", newUser.getLastName());
        namedParameters.addValue("role", newUser.getRole());

        try {
            jdbcTemplate.update("INSERT INTO user (username, email, password, first_name, last_name, role) VALUES (:username, :email, :password, :firstName, :lastName, :role)", namedParameters);
            return getUser(newUser.getUsername());
        } catch (DataAccessException dae) {
            throw dae;
        }
    }

    private User getUser(String username) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("username", username);

        return jdbcTemplate.queryForObject("SELECT * FROM user WHERE username = :username", namedParameters, new BeanPropertyRowMapper<>(User.class));
    }

    public List<User> getLoginUser(User loginUser) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        if (loginUser.getUsername().contains("@")) {
            loginUser.setEmail(loginUser.getUsername());
        }
        namedParameters.addValue("username", loginUser.getUsername());
        namedParameters.addValue("email", loginUser.getEmail());

        return jdbcTemplate.query("SELECT * FROM user WHERE username = :username OR email = :email", namedParameters, new BeanPropertyRowMapper<>(User.class));
    }

    public User getLoginUserObject(User loginUser) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("username", loginUser.getUsername());
        namedParameters.addValue("email", loginUser.getEmail());

        return (User) jdbcTemplate.queryForObject("SELECT id, username, email, password, first_name, last_name, role FROM user WHERE username = :username OR email = :email", namedParameters, new BeanPropertyRowMapper(User.class));
    }

    public User getUserById(long id) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("id", id);
        return (User) jdbcTemplate.queryForObject("SELECT * FROM user WHERE id = :id", namedParameters, new BeanPropertyRowMapper<>(User.class));
    }

    public User updateUserRole(long id, String role) {
        if (role.isEmpty()) {
            role = null;
        }
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("id", id);
        namedParameters.addValue("role", role);
        jdbcTemplate.update("UPDATE user SET role = :role WHERE id = :id", namedParameters);
        return getUserById(id);
    }
}
