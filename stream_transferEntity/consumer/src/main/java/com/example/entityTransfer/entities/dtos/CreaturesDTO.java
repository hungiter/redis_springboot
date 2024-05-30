package com.example.entityTransfer.entities.dtos;

import com.example.entityTransfer.entities.models.CreaturesSkill;
import com.example.entityTransfer.entities.utils.EnumUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CreaturesDTO {
    String type;
    String color;
    boolean gender;
    List<CreaturesSkill> skills;

    public CreaturesDTO(String type, String color, boolean gender, List<CreaturesSkill> skills) {
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
        return skills.stream()
                .map(CreaturesSkill::name)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    // Private string processors
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
        return getSkills().stream()
                .map(EnumUtil::convertEnumName)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public int totalPower() {
        return skills.stream()
                .mapToInt(CreaturesSkill::getPower)
                .sum();
    }

    private String creatureInfo() {
        return creatureColor() + " " + creatureType() + "(" + creatureGender() + ")";
    }

    @Override
    public String toString() {
        return creatureInfo() + "\n   Skills:"
                + creatureSkills().stream().map(skill -> "\n    " + skill).collect(Collectors.joining())
                + "\nTotal power: " + totalPower();
    }
}
