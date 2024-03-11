package org.example.userbackend.controller;

import org.example.userbackend.model.Ride;
import org.example.userbackend.repository.RideRepository;
import org.example.userbackend.service.RideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/ride")
@CrossOrigin("http://localhost:4200")
public class RideController {
    @Autowired
    private RideRepository rideRepository;

    @Autowired
    private RideService rideService;

    @GetMapping("/getRideByUserId/{userId}")
    public List<Ride> getByUserId(@PathVariable Long userId) {
        return rideRepository.getRideById(userId);
    }

    @PostMapping("/addRideByUser/{userId}")
    public ResponseEntity<Ride> addRideByUser(@PathVariable Long userId, @RequestBody Ride ride) {
        ride.setUserId(userId);
        ride.setDateTime(LocalDateTime.now());
        return rideService.addRideByUser(userId, ride);
    }
}
