package com.example.personalityapi;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class LobbyService {

    @Autowired
    private LobbyRepository lobbyRepository;

    @Autowired
    private UserRepository userRepository;


    // Create a new lobby and assign admin = true
    public Lobby createLobby(Long userId, LocationDTO location) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Long newLobbyId = System.currentTimeMillis(); // simple unique ID
        Lobby lobby = new Lobby();
        lobby.setLobbyId(newLobbyId);
        lobby.setUser(user);
        lobby.setAdmin(true);
        lobby.setLatitude(location.getLatitude());
        lobby.setLongitude(location.getLongitude());

        return lobbyRepository.save(lobby);
    }

    // Join existing lobby as non-admin
    public Lobby joinLobby(Long userId, Long lobbyId,LocationDTO location) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean exists = lobbyRepository.existsByUser_UserIdAndLobbyId(userId, lobbyId);
        if (exists) {
            throw new RuntimeException("User already in the lobby");
        }

        Lobby lobby = new Lobby();
        lobby.setLobbyId(lobbyId);
        lobby.setUser(user);
        lobby.setAdmin(false);
        lobby.setLatitude(location.getLatitude());
        lobby.setLongitude(location.getLongitude());

        return lobbyRepository.save(lobby);
    }

    // Analyze the group and recommend destination
    public List<UserInfo> searchDestination(Long lobbyId) {
        List<Lobby> lobbyUsers = lobbyRepository.findByLobbyId(lobbyId);
        if (lobbyUsers.isEmpty()) {
            throw new RuntimeException("Lobby not found or empty");
        }

        List<LobbyUsersDTO> lobbyUsersDTO = lobbyUsers.stream().map(lobby -> {
            User user = lobby.getUser();
            return new LobbyUsersDTO(
                    user.getUserId(),
                    user.getName(),
                    user.getEmail(),
                    user.getAnimal(),
                    user.getPersonality(),
                    lobby.getLatitude(),      // Take from Lobby row
                    lobby.getLongitude()
            );
        }).collect(Collectors.toList());;

        Gson gson = new Gson();

        return lobbyUsers.stream()
                .map(dto -> {
                    Personality personality = null;
                    try {
                        personality = gson.fromJson(dto.getUser().getPersonality(), Personality.class);
                    } catch (Exception e) {
                        System.err.println("Invalid personality JSON for user: " + dto.getUser().getUserId());
                    }
                    return new UserInfo(
                            dto.getUser().getUserId(),
                            personality,
                            dto.getLatitude(),
                            dto.getLongitude()
                    );
                })
                .collect(Collectors.toList());
    }

}
