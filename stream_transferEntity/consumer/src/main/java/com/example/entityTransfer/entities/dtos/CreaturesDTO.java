package com.example.entityTransfer.entities.dtos;

import com.example.entityTransfer.entities.models.CreaturesSkill;
import com.example.entityTransfer.entities.utils.EnumUtil;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreaturesDTO {
    String type;
    String color;
    boolean gender;
    List<CreaturesSkill> skills;



    // Self-defined
    public List<String> getSkills() {
        return skills.stream()
                .map(CreaturesSkill::name)
                .collect(Collectors.toCollection(ArrayList::new));
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
