package com.example.personalityapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.xml.stream.Location;
import java.util.List;

@RestController
@RequestMapping("/api/lobby")
public class LobbyController {

    @Autowired
    private LobbyService lobbyService;
    @Autowired
    private GeminiService geminiService;

    // 1. Create a new lobby
    @CrossOrigin(origins = "*") // Allow all origins
    @PostMapping("/create/{userId}")
    public Lobby createLobby(@PathVariable Long userId,
                             @RequestBody LocationDTO location) {
        return lobbyService.createLobby(userId,location);
    }

    // 2. Join an existing lobby
    @CrossOrigin(origins = "*") // Allow all origins
    @PostMapping("/join/{lobbyId}/{userId}")
    public Lobby joinLobby(@PathVariable Long lobbyId, @PathVariable Long userId,@RequestBody LocationDTO location) {
        return lobbyService.joinLobby(userId, lobbyId,location);
    }

    // 3. Search for best destination based on group
    @CrossOrigin(origins = "*") // Allow all origins
    @GetMapping("/search/{lobbyId}")
    public DesiredCity search(@PathVariable Long lobbyId) throws InterruptedException {
         List<UserInfo> users =  lobbyService.searchDestination(lobbyId);
         return geminiService.suggestGroupDestination(users);
    }


}
