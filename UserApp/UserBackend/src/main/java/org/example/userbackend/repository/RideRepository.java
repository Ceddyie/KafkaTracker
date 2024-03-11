package org.example.userbackend.repository;

import org.example.userbackend.model.Ride;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RideRepository {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    public List<Ride> getRideById(Long userId) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("user_id", userId);

        return jdbcTemplate.query("SELECT * FROM ride WHERE user_id = :user_id", namedParameters, new BeanPropertyRowMapper<>(Ride.class));
    }

    public Ride addRideByUser(Ride ride) {
        ride.setStatus("SENT");
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("user_id", ride.getUserId());
        namedParameters.addValue("current_address", ride.getCurrentAddress());
        namedParameters.addValue("current_city", ride.getCurrentCity());
        namedParameters.addValue("destination_address", ride.getDestinationAddress());
        namedParameters.addValue("destination_city", ride.getDestinationCity());
        namedParameters.addValue("status", ride.getStatus());
        namedParameters.addValue("date_time", ride.getDateTime());

        jdbcTemplate.update("INSERT INTO ride (user_id, current_address, current_city, destination_address, destination_city, status, date_time) VALUES" +
                " (:user_id, :current_address, :current_city, :destination_address, :destination_city, :status, :date_time)", namedParameters);
        return new ResponseEntity<Ride>(ride, HttpStatus.OK).getBody();
    }
}
