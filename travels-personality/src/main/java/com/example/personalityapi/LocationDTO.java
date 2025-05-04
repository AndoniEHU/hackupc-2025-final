package com.example.personalityapi;

import lombok.*;
import lombok.Data;

@Getter
@Setter
@NoArgsConstructor
@Data
@AllArgsConstructor
public class LocationDTO {
    private double latitude;
    private double longitude;
}
