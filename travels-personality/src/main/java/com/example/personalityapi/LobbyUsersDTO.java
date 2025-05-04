package com.example.personalityapi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class LobbyUsersDTO {
    private Long userId;
    private String name;
    private String email;
    private String animal;
    private String personality;

    private double latitude;  // From Lobby
    private double longitude; // From Lobby
}
