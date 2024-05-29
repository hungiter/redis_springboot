package com.example.entityTransfer.entities.models_database;

import jakarta.persistence.*;


import java.util.List;

@Entity
@Table(name = "Creatures")
public class H2Creatures {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type", nullable = false)
    private final String type;

    @Column(name = "color", nullable = false)
    private final String color;

    @Column(name = "gender", nullable = false)
    private final boolean gender;

    @Column(name = "skills", nullable = false)
    private final List<String> skills;

    @Column(name = "quantity", nullable = false)
    private final int quantity;


    public H2Creatures(String type, String color, boolean gender, List<String> skills, int quantity) {
        this.type = type;
        this.color = color;
        this.gender = gender;
        this.skills = skills;
        this.quantity = quantity;
    }

    public String getType() {
        return type;
    }

    public String getColor() {
        return color;
    }

    public boolean isGender() {
        return gender;
    }

    public List<String> getSkills() {
        return skills;
    }

    public int getQuantity() {
        return quantity;
    }
}
