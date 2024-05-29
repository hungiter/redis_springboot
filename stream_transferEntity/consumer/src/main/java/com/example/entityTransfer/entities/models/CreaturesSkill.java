package com.example.entityTransfer.entities.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public enum CreaturesSkill {
    // Dragon Skills
    FIRE_BREATHING(CreatureType.DRAGON),
    FLIGHT(CreatureType.DRAGON),
    IMMENSE_STRENGTH(CreatureType.DRAGON),
    DRAGON_MAGIC(CreatureType.DRAGON),
    LONGEVITY(CreatureType.DRAGON),

    // Griffin Skills
    ENHANCED_VISION(CreatureType.GRIFFIN),
    GRIFFIN_STRENGTH(CreatureType.GRIFFIN),
    GUARDIANSHIP(CreatureType.GRIFFIN),
    GRIFFIN_AGILITY(CreatureType.GRIFFIN),

    // Unicorn Skills
    HEALING(CreatureType.UNICORN),
    PURITY_GRACE(CreatureType.UNICORN),
    UNICORN_SPEED(CreatureType.UNICORN),
    UNICORN_MAGIC(CreatureType.UNICORN),
    IMMORTALITY(CreatureType.UNICORN),

    // Phoenix Skills
    REBIRTH(CreatureType.PHOENIX),
    FIRE_MANIPULATION(CreatureType.PHOENIX),
    HEALING_TEARS(CreatureType.PHOENIX),
    PHOENIX_LONGEVITY(CreatureType.PHOENIX),
    PHOENIX_FLIGHT(CreatureType.PHOENIX),

    // Queen Mab Skills
    DREAM_MANIPULATION(CreatureType.QUEEN_MAB),
    FAIRY_MAGIC(CreatureType.QUEEN_MAB),
    ILLUSION_CREATION(CreatureType.QUEEN_MAB),
    SHAPE_SHIFTING(CreatureType.QUEEN_MAB),
    CONTROL_OVER_NATURE(CreatureType.QUEEN_MAB);

    private final CreatureType creatureType;

    CreaturesSkill(CreatureType creatureType) {
        this.creatureType = creatureType;
    }

    public CreatureType getCreatureType() {
        return creatureType;
    }

    public static List<CreaturesSkill> getRandomSkills(CreatureType type) {
        Random random = new Random();
        List<CreaturesSkill> availableSkills = Arrays.stream(values())
                .filter(skill -> skill.getCreatureType() == type)
                .toList();
        List<CreaturesSkill> skills = new ArrayList<>();

        while (skills.size() < (type == CreatureType.GRIFFIN ? 2 : 3)) {
            CreaturesSkill randomSkill = availableSkills.get(random.nextInt(availableSkills.size()));
            if (!skills.contains(randomSkill)) {
                skills.add(randomSkill);
            }
        }

        return skills;

    }
}
