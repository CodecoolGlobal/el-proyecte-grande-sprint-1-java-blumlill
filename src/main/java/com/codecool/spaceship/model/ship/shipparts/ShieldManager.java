package com.codecool.spaceship.model.ship.shipparts;


import com.codecool.spaceship.model.Upgradeable;
import com.codecool.spaceship.model.UpgradeableType;
import com.codecool.spaceship.model.exception.InvalidLevelException;
import com.codecool.spaceship.service.LevelService;

public class ShieldManager extends Upgradeable {

    private static final UpgradeableType type = UpgradeableType.SHIELD;
    private int currentEnergy;

    public ShieldManager(LevelService levelService, int currentLevel, int currentEnergy) {
        super(levelService, type, currentLevel);
        if (currentEnergy < 0) {
            throw new IllegalArgumentException("Shield energy can't be lower than 0");
        } else if (currentEnergy > super.currentLevel.getEffect()) {
            throw new InvalidLevelException("Shield energy can't be higher than %d at this level".formatted(super.currentLevel.getEffect()));
        }
        this.currentEnergy = currentEnergy;
    }
    public ShieldManager(LevelService levelService) {
        super(levelService, type, 1);
        currentEnergy = super.currentLevel.getEffect();
    }

    public int getMaxEnergy() {
        return currentLevel.getEffect();
    }

    public int getCurrentEnergy() {
        return currentEnergy;
    }

    public void repair(int amount) {
        currentEnergy = Math.min(currentEnergy + amount, getMaxEnergy());
    }

    public void damage(int amount) {
        currentEnergy = Math.max(currentEnergy - amount, 0);
    }
}
