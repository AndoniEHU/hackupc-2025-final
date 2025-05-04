
package com.example.personalityapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class GeminiService {

    @Value("${gemini.api.url}")
    private String apiUrl;

    @Value("${gemini.api.key}")
    private String apiKey;

    // Now you can build the full request URL with key
    private String getFullUrl() {
        return apiUrl + "?key=" + apiKey;
    }
    private final RestTemplate restTemplate = new RestTemplate();

    public Data getPersonalityFromGemini(String... answers) {
        // Format the prompt with user answers
        String prompt = String.format("""
                    Analyze the profile of a user in base to their answers to a form with the questions and answers:
                    1. You’re alone in a jungle, what do you do?. User answer: %s
                    2. When thinking about your desired home, what do you picture?. User answer: %s
                    3. You end up stranded in an island, what of these items do you choose to bring?. User answer: %s
                    4. What is your favourite holiday?. User answer: %s
                    5. Which of these three sets of characteristics best defines your personality?. User answer: %s
                    6. What type of sport do you prefer?. User answer: %s
                    7. If you could only choose one type of food to eat, it would be: (user answer) %s
                
                    The output MUST be on JSON format, with exactly the following fields:
                    - animal: Just the name of the animal that better represents the user based on their answers.
                    - personality: an object with fields events, style and money.
                    It will look like this:
                    {
                      animal: "animal name",
                      personality: {
                        events: ["football", "museum", "concert"],
                        style: "nightlife",
                        money: "cheap"
                      }
                    }
                """, (Object[]) answers);

        String body = String.format("""
                    {
                      "contents": [
                        {
                          "parts": [
                            { "text": %s }
                          ]
                        }
                      ]
                    }
                """, toJsonString(prompt));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        // Build full URL with API key
        String fullUrl = apiUrl + "?key=" + apiKey;

        // Perform POST request
        ResponseEntity<String> response = restTemplate.exchange(
                fullUrl,
                HttpMethod.POST,
                entity,
                String.class
        );

        // Extract JSON result from Gemini response
        String textContent = JsonParser.parseString(response.getBody())
                .getAsJsonObject()
                .getAsJsonArray("candidates")
                .get(0).getAsJsonObject()
                .getAsJsonObject("content")
                .getAsJsonArray("parts")
                .get(0).getAsJsonObject()
                .get("text").getAsString();

        textContent = textContent.replaceAll("```json", "").replaceAll("```", "").trim();
        ObjectMapper mapper = new ObjectMapper();
        Data data;
        try {
            data = mapper.readValue(textContent, Data.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return data;
    }

    public DesiredCity suggestGroupDestination(List<UserInfo> users) throws InterruptedException {

        StringBuilder prompt = new StringBuilder("""
        You are a travel expert. Analyze the following group of travelers and suggest the most suitable travel destination for them.

        Each traveler has personality traits including preferred events, travel style, and budget.

        Here is their combined profile:
        """);

        int index = 1;
        for (UserInfo user : users) {

            Personality p = user.getPersonality();
            prompt.append(String.format("""
        
        Traveler %d:
        - Events: %s
        - Style: %s
        - Money: %s
        """, index++, String.join(", ", p.getEvents()), p.getStyle(), p.getMoney()));
        }

        prompt.append("""

        Based on the commonalities and compatibility across their personalities, recommend:
        - One specific travel destination

        Output format:
        {
          "destination": "City",
          "country": "Country"
        }
        """);

        String body = String.format("""
                    {
                      "contents": [
                        {
                          "parts": [
                            { "text": %s }
                          ]
                        }
                      ]
                    }
                """, toJsonString(String.valueOf(prompt)));

        // Send prompt to Gemini

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        // Build full URL with API key
        String fullUrl = apiUrl + "?key=" + apiKey;

        // Perform POST request
        ResponseEntity<String> response = restTemplate.exchange(
                fullUrl,
                HttpMethod.POST,
                entity,
                String.class
        );
        String textContent = JsonParser.parseString(response.getBody())
                .getAsJsonObject()
                .getAsJsonArray("candidates")
                .get(0).getAsJsonObject()
                .getAsJsonObject("content")
                .getAsJsonArray("parts")
                .get(0).getAsJsonObject()
                .get("text").getAsString();

        textContent = textContent.replaceAll("```json", "").replaceAll("```", "").trim();
        ObjectMapper mapper = new ObjectMapper();
        DesiredCity city;
        try {
            city = mapper.readValue(textContent, DesiredCity.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return city;
    }


    private String toJsonString(String rawText) {
        return "\"" + rawText.replace("\"", "\\\"").replace("\n", "\\n") + "\"";
    }
}
