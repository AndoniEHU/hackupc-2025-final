package com.example.personalityapi;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LobbyRepository extends JpaRepository<Lobby, Long> {
    List<Lobby> findByLobbyId(Long lobbyId);
    boolean existsByUser_UserIdAndLobbyId(Long userId, Long lobbyId);
}
