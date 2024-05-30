package com.example.entityTransfer.entities.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public enum CreaturesSkill {
    // Dragon Skills
    FIRE_BREATHING(CreatureType.DRAGON,100),
    FLIGHT(CreatureType.DRAGON,150),
    IMMENSE_STRENGTH(CreatureType.DRAGON,200),
    DRAGON_MAGIC(CreatureType.DRAGON,200),
    LONGEVITY(CreatureType.DRAGON,300),

    // Griffin Skills
    ENHANCED_VISION(CreatureType.GRIFFIN,50),
    GRIFFIN_STRENGTH(CreatureType.GRIFFIN,150),
    GUARDIANSHIP(CreatureType.GRIFFIN,100),
    GRIFFIN_AGILITY(CreatureType.GRIFFIN,100),

    // Unicorn Skills
    HEALING(CreatureType.UNICORN,150),
    PURITY_GRACE(CreatureType.UNICORN,100),
    UNICORN_SPEED(CreatureType.UNICORN,100),
    UNICORN_MAGIC(CreatureType.UNICORN,150),
    IMMORTALITY(CreatureType.UNICORN,200),

    // Phoenix Skills
    REBIRTH(CreatureType.PHOENIX,300),
    FIRE_MANIPULATION(CreatureType.PHOENIX,150),
    HEALING_TEARS(CreatureType.PHOENIX,150),
    PHOENIX_LONGEVITY(CreatureType.PHOENIX,300),
    PHOENIX_FLIGHT(CreatureType.PHOENIX,150),

    // Queen Mab Skills
    DREAM_MANIPULATION(CreatureType.QUEEN_MAB,150),
    FAIRY_MAGIC(CreatureType.QUEEN_MAB,150),
    ILLUSION_CREATION(CreatureType.QUEEN_MAB,200),
    SHAPE_SHIFTING(CreatureType.QUEEN_MAB,200),
    CONTROL_OVER_NATURE(CreatureType.QUEEN_MAB,300);

    private final CreatureType creatureType;
    private final int power;

    CreaturesSkill(CreatureType creatureType,int power) {
        this.creatureType = creatureType;
        this.power = power;
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

    public int getPower() {
        return power;
    }
}
