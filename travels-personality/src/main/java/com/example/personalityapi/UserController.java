
package com.example.personalityapi;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @CrossOrigin(origins = "*") // Allow all origins
    @PostMapping("/register")
    public String register(@RequestBody @Valid LoginRequest request) {
        User user = userService.registerUser(request);
        return Long.toString(user.getUserId());
    }

    @CrossOrigin(origins = "*") // Allow all origins
    @PostMapping("/login")
    public String login(@RequestBody LoginCredentials credentials) {
        return userService.login(credentials);
    }

    @CrossOrigin(origins = "*") // Allow all origins
    @PostMapping("/analyze")
    public String analyze(@RequestBody PersonalityRequest request) {
        return userService.analyzePersonality(request);
    }
}
