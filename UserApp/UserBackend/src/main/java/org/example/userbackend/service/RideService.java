package org.example.userbackend.service;

import org.example.userbackend.model.Ride;
import org.example.userbackend.repository.RideRepository;
import org.example.userbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class RideService {
    @Autowired
    private RideRepository rideRepository;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public ResponseEntity<Ride> addRideByUser(Long userId, Ride ride) {
        kafkaTemplate.send("ride-request", ride);
        return new ResponseEntity<Ride>(rideRepository.addRideByUser(ride), HttpStatus.OK);
    }
}
