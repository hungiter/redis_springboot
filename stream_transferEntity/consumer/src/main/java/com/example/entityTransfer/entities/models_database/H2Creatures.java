package com.example.entityTransfer.entities.models_database;

import jakarta.persistence.*;
import lombok.*;


import java.util.List;

@Entity
@Table(name = "Creatures")
@Data
@Getter
@Setter
@ToString
@NoArgsConstructor
public class H2Creatures {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "color", nullable = false)
    private String color;

    @Column(name = "gender", nullable = false)
    private boolean gender;

    @Column(name = "skills", nullable = false)
    private List<String> skills;

    public H2Creatures(String type, String color, boolean gender, List<String> skills) {
        this.type = type;
        this.color = color;
        this.gender = gender;
        this.skills = skills;
    }
}
