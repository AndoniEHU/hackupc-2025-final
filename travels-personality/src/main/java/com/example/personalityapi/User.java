
package com.example.personalityapi;

import jakarta.persistence.*;
import lombok.*;
import lombok.Data;

@Entity
@Data
@NoArgsConstructor
@Table(name = "usuarios")
@AllArgsConstructor
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String name;
    private String surname;
    private String country;
    private String email;
    private String password;

    private String animal;

    @Column(length = 2000)
    private String personality;

    private double latitude;
    private double longitude;

    public User(Long userId, String name, String surname, String country, String email, String password, String animal, String personality) {
        this.userId = userId;
        this.name = name;
        this.surname = surname;
        this.country = country;
        this.email = email;
        this.password = password;
        this.animal = animal;
        this.personality = personality;
    }
}
