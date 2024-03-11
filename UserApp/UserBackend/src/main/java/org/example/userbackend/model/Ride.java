package org.example.userbackend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Entity
@Table
public class Ride {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long rideId;

    @Column
    private long userId;

    @Column
    private String currentAddress;

    @Column
    private String currentCity;

    @Column
    private String destinationAddress;

    @Column
    private String destinationCity;

    @Column
    private String status;

    @Column
    private LocalDateTime dateTime;
}
