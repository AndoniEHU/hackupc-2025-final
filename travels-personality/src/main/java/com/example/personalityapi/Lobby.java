package com.example.personalityapi;

import jakarta.persistence.*;
import lombok.*;
import lombok.Data;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "lobby")
public class Lobby {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long lobbyId;

    @ManyToOne
    private User user;

    private boolean admin;

    private double latitude;
    private double longitude;
}

