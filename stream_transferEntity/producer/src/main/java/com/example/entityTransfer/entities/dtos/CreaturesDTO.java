package com.example.entityTransfer.entities.dtos;

import com.example.entityTransfer.entities.utils.EnumUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CreaturesDTO {
    String type;
    String color;
    boolean gender;
    List<String> skills;

    public CreaturesDTO(String type, String color, boolean gender, List<String> skills) {
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

    public boolean getGender() {
        return gender;
    }

    public List<String> getSkills() {
        return skills;
    }

    private String creatureType() {
        return EnumUtil.convertEnumName(type);
    }

    private String creatureColor() {
        return EnumUtil.convertEnumName(color);
    }

    private String creatureGender() {
        return gender ? "♂" : "♀";
    }

    private List<String> creatureSkills() {
        return skills.stream()
                .map(EnumUtil::convertEnumName)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private String creatureInfo() {
        return creatureGender() + " " + creatureColor() + " " + creatureType();
    }

    @Override
    public String toString() {
        return "Creature's info:{" + creatureInfo() + "'s skills: "
                + creatureSkills().stream().map(skill -> "\n  " + skill).collect(Collectors.joining());
    }
}
