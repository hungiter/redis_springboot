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
    private String type;

    @Column(name = "color", nullable = false)
    private String color;

    @Column(name = "gender", nullable = false)
    private boolean gender;

    @Column(name = "skills", nullable = false)
    private List<String> skills;

    public H2Creatures() {
    }

    public H2Creatures(String type, String color, boolean gender, List<String> skills) {
        this.type = type;
        this.color = color;
        this.gender = gender;
        this.skills = skills;
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

    public void setType(String type) {
        this.type = type;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }
}
