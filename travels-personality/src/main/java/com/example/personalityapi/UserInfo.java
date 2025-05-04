package com.example.personalityapi;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {

    private Long userId;
    private Personality personality;
    private double latitude;  // From Lobby
    private double longitude; // From Lobby
}
