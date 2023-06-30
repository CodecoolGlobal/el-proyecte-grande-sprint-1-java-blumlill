package com.codecool.spaceship.model.ship.shipparts;

import com.codecool.spaceship.model.Upgradeable;
import com.codecool.spaceship.model.UpgradeableType;
import com.codecool.spaceship.service.LevelService;

public class EngineManager extends Upgradeable {

    private static final UpgradeableType type = UpgradeableType.ENGINE;

    public EngineManager(LevelService levelService, int currentLevel) {
        super(levelService, type, currentLevel);
    }

    public EngineManager(LevelService levelService) {
        this(levelService, 1);
    }

    public int getCurrentLevel() {
        return currentLevel.getLevel();
    }

    public int getSpeed() {
        return currentLevel.getEffect();
    }
}
