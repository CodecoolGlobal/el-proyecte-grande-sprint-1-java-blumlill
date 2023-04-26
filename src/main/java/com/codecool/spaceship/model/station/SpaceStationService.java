package com.codecool.spaceship.model.station;

import com.codecool.spaceship.model.Resource;
import com.codecool.spaceship.model.Upgradeable;
import com.codecool.spaceship.model.exception.NoSuchPartException;
import com.codecool.spaceship.model.exception.StorageException;
import com.codecool.spaceship.model.exception.UpgradeNotAvailableException;
import com.codecool.spaceship.model.ship.SpaceShipService;
import com.codecool.spaceship.model.ship.shipparts.ShipPart;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class SpaceStationService {
    private String name;
    //private User user;
    private final UUID id;
    private final SpaceStationStorage storage;
    private final HangarService hangarService;

    public SpaceStationService(String name) {
        this.name = name;
        this.id = UUID.randomUUID();
        this.storage = new SpaceStationStorage();
        this.hangarService = new HangarService();
    }
    /*
    public void addFirstShip() {
        try {
            hangar.addShip(new MinerShip("Eeny Meeny Miny Moe", Color.EMERALD));
        } catch (StorageException ignored) {
        }
    }*/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SpaceStationStorage getStorage() {
        return storage;
    }

    public HangarService getHangar() {
        return hangarService;
    }

    private boolean hasEnoughResource(Map<Resource, Integer> cost) {
        return cost.entrySet().stream().allMatch(entry -> storage.hasResource(entry.getKey(), entry.getValue()));
    }

    private boolean removeResources(Map<Resource, Integer> cost) throws StorageException {
        if (hasEnoughResource(cost)) {
            for (Resource resource : cost.keySet()) {
                storage.removeResource(resource, cost.get(resource));
            }
            return true;
        }
        throw new StorageException("Not enough resource");
    }

    public boolean addNewShip(SpaceShipService ship) throws StorageException {
        Map<Resource, Integer> cost = ship.getCost();
        return hangarService.addShip(ship) && removeResources(cost); //throws storage exception if not enough resource or docks
    }

    public boolean deleteShip(SpaceShipService ship){
        return hangarService.removeShip(ship);
    }

    public Set<SpaceShipService> getAllShips() {
        return new HashSet<>(hangarService.getAllShips());
    }

    public boolean upgradeShipPart(SpaceShipService ship, ShipPart shipPart) throws NoSuchPartException, UpgradeNotAvailableException, StorageException {
        if (!hangarService.getAllShips().contains(ship)) throw new StorageException("No such ship in storage");
        if (!ship.isAvailable()) throw new UpgradeNotAvailableException("Ship is on a mission");
        Upgradeable part = ship.getPart(shipPart);
        Map<Resource, Integer> cost = part.getUpgradeCost();
        removeResources(cost);
        part.upgrade();
        return true;
    }

    public boolean addResource(Resource resource, int quantity) throws StorageException {
        return storage.addResource(resource, quantity);
    }

    public boolean upgradeStorage() throws UpgradeNotAvailableException, StorageException {
        Map<Resource, Integer> cost = storage.getUpgradeCost();
        removeResources(cost);
        storage.upgrade();
        return true;
    }

    public boolean upgradeHangar() throws UpgradeNotAvailableException, StorageException {
        Map<Resource, Integer> cost = hangarService.getUpgradeCost();
        removeResources(cost);
        hangarService.upgrade();
        return true;
    }
}