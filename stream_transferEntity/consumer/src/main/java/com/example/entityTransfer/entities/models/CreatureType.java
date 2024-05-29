package com.example.entityTransfer.entities.models;

import java.util.Arrays;
import java.util.List;

public enum CreatureType {
    DRAGON(Arrays.asList(CreaturesColor.RED, CreaturesColor.BLACK, CreaturesColor.GOLD)),
    GRIFFIN(Arrays.asList(CreaturesColor.GOLD, CreaturesColor.WHITE, CreaturesColor.BROWN)),
    UNICORN(Arrays.asList(CreaturesColor.WHITE, CreaturesColor.SILVER, CreaturesColor.PURPLE)),
    PHOENIX(Arrays.asList(CreaturesColor.RED, CreaturesColor.ORANGE, CreaturesColor.GOLD)),
    QUEEN_MAB(Arrays.asList(CreaturesColor.GREEN, CreaturesColor.BLUE, CreaturesColor.SILVER));

    private final List<CreaturesColor> creaturesColors;

    CreatureType(List<CreaturesColor> creaturesColors) {
        this.creaturesColors = creaturesColors;
    }

    public List<CreaturesColor> getColors() {
        return creaturesColors;
    }
}
