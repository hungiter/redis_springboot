package com.example.entityTransfer.entities.models;

import com.example.entityTransfer.entities.dtos.CreaturesDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RedisHash("creatures")
@Data
@NoArgsConstructor
public class Creatures {
    CreatureType type;
    CreaturesColor color;
    Gender gender;
    List<CreaturesSkill> skills;

    public Creatures(CreatureType type, CreaturesColor color, Gender gender, List<CreaturesSkill> skills) {
        this.type = type;
        this.color = color;
        this.gender = gender;
        this.skills = skills;
    }

    public CreaturesDTO creatureInfo() {
        return new CreaturesDTO(this.type.name(), this.color.name(), this.gender.name().equals("MALE"), skills.stream()
                .map(CreaturesSkill::name)
                .collect(Collectors.toCollection(ArrayList::new)));
    }
}
