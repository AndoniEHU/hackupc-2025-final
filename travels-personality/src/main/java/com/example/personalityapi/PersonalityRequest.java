
package com.example.personalityapi;

import lombok.Data;

@Data
public class PersonalityRequest {
    private Long userId;
    private String q1;
    private String q2;
    private String q3;
    private String q4;
    private String q5;
    private String q6;
    private String q7;
}
