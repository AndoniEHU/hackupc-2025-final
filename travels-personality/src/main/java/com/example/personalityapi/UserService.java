
package com.example.personalityapi;

import com.google.gson.JsonObject;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GeminiService geminiService;

    public String login(LoginCredentials credentials) {
        User user = userRepository.findByEmail(credentials.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getPassword().equals(credentials.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return Long.toString(user.getUserId());
    }

    public User registerUser(LoginRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("Passwords do not match.");
        }

        User user = new User();
        user.setName(request.getName());
        user.setSurname(request.getSurname());
        user.setCountry(request.getCountry());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());

        return userRepository.save(user);
    }
    @Transactional
    public String analyzePersonality(PersonalityRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Data geminiResponse = geminiService.getPersonalityFromGemini(
                request.getQ1(), request.getQ2(), request.getQ3(),
                request.getQ4(), request.getQ5(), request.getQ6(), request.getQ7());

        String animal = geminiResponse.getAnimal();
        Personality personality = geminiResponse.getPersonality()   ;

        String personalityString = personality.toString();

        user.setAnimal(animal);
        user.setPersonality(personalityString);
        System.out.println(user.getAnimal() + "." + user.getPersonality());
        userRepository.save(user);

        return animal;
    }
}
