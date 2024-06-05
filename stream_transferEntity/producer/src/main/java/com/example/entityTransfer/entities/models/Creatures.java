package com.example.entityTransfer.entities.models;

import com.example.entityTransfer.entities.dtos.CreaturesDTO;
import lombok.*;
import org.springframework.data.redis.core.RedisHash;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RedisHash("creatures")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Creatures {
    CreatureType type;
    CreaturesColor color;
    Gender gender;
    List<CreaturesSkill> skills;

    //Self-defined
    public CreaturesDTO creatureInfo() {
        return new CreaturesDTO(this.type.name(), this.color.name(), this.gender.name().equals("MALE"), this.skills);
    }
}
