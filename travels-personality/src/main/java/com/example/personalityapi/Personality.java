package com.example.personalityapi;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Personality {
    public List<String> events;
    public String style;
    public String money;

    @Override
    public String toString() {
        return String.format(
                "{\n  \"events\": %s,\n  \"style\": \"%s\",\n  \"money\": \"%s\"\n}",
                new Gson().toJson(events),  // this ensures ["A", "B", "C"] format
                style,
                money
        );
    }
}
