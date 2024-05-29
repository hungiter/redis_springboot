package com.example.entityTransfer.entities.models;

import com.example.entityTransfer.entities.utils.EnumUtil;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Random;

@Repository
public class CreaturesRepository {
    public Creatures getRandomCreatures() {
        Random random = new Random();

        CreatureType type = EnumUtil.getRandomEnumValue(CreatureType.class);
        List<CreaturesSkill> skills = CreaturesSkill.getRandomSkills(type);
        Gender gender = EnumUtil.getRandomEnumValue(Gender.class);
        List<CreaturesColor> colors = type.getColors();
        CreaturesColor color = colors.get(random.nextInt(colors.size()));
        return new Creatures(type,color,gender,skills);
    }
}
