package com.codecool.spaceship.service;

import com.codecool.spaceship.model.UserEntity;
import com.codecool.spaceship.model.dto.MinerShipDTO;
import com.codecool.spaceship.model.dto.ShipDTO;
import com.codecool.spaceship.model.exception.NoSuchPartException;
import com.codecool.spaceship.model.exception.ShipNotFoundException;
import com.codecool.spaceship.model.exception.StorageException;
import com.codecool.spaceship.model.exception.UpgradeNotAvailableException;
import com.codecool.spaceship.model.resource.ResourceType;
import com.codecool.spaceship.model.ship.MinerShip;
import com.codecool.spaceship.model.ship.MinerShipManager;
import com.codecool.spaceship.model.ship.ShipType;
import com.codecool.spaceship.model.ship.SpaceShip;
import com.codecool.spaceship.model.ship.shipparts.Color;
import com.codecool.spaceship.model.ship.shipparts.ShipPart;
import com.codecool.spaceship.model.station.SpaceStationManager;
import com.codecool.spaceship.repository.SpaceShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ShipService {
    private final SpaceShipRepository spaceShipRepository;

    @Autowired
    public ShipService(SpaceShipRepository spaceShipRepository) {
        this.spaceShipRepository = spaceShipRepository;
    }

    public List<ShipDTO> getShipsByStation(long stationId) {
        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))
                && user.getSpaceStation().getId() != stationId) {
            throw new SecurityException("You don't have authority to access these ships");
        }
        return spaceShipRepository.getSpaceShipsByStationId(stationId).stream()
                .map(ShipDTO::new)
                .collect(Collectors.toList());
    }

    public ShipDTO getShipByID(long id) throws ShipNotFoundException {
        return new ShipDTO(getShipByIdAndCheckAccess(id));
    }

    public MinerShipDTO getMinerShipById(long id) throws ShipNotFoundException, IllegalArgumentException {
        SpaceShip ship = getShipByIdAndCheckAccess(id);

        if (ship instanceof MinerShip) {
            return new MinerShipDTO((MinerShip) ship);
        } else {
            throw new IllegalArgumentException("Ship is not a miner ship.");
        }
    }

    public MinerShipDTO upgradeMinerShip(Long id, ShipPart part) throws ShipNotFoundException, UpgradeNotAvailableException, NoSuchPartException, StorageException {
        SpaceShip ship = getShipByIdAndCheckAccess(id);

        if (ship instanceof MinerShip) {
            MinerShipManager minerShipManager = new MinerShipManager((MinerShip) ship);

            SpaceStationManager stationManager = new SpaceStationManager(ship.getStation());
            if (stationManager.hasShipAvailable(ship)) {
                stationManager.removeResources(minerShipManager.getUpgradeCost(part));
                minerShipManager.upgradePart(part);
                ship = spaceShipRepository.save(ship);
            }
            return new MinerShipDTO((MinerShip) ship);
        } else {
            throw new IllegalArgumentException("Ship is not a miner ship");
        }
    }

    public ShipDTO updateShipAttributes(Long id, String name, Color color) throws ShipNotFoundException {
        SpaceShip ship = getShipByIdAndCheckAccess(id);

        if (name != null && !name.equals("")) {
            ship.setName(name);
        }
        if (color != null) {
            ship.setColor(color);
        }
        ship = spaceShipRepository.save(ship);
        return new ShipDTO(ship);
    }

    public Map<ResourceType, Integer> getShipCost(ShipType shipType) {
        if (shipType != null) {
            return shipType.getCost();
        } else {
            return null;
        }
    }

    public boolean deleteShipById(Long id) throws ShipNotFoundException, StorageException {
        SpaceShip ship = getShipByIdAndCheckAccess(id);

        if (ship.isOnMission()) {
            throw new StorageException("Ship can't be deleted while on mission");
        }
        spaceShipRepository.delete(ship);
        return true;
    }

    private SpaceShip getShipByIdAndCheckAccess(Long id) throws ShipNotFoundException {
        SpaceShip ship = spaceShipRepository.findById(id)
                .orElseThrow(() -> new ShipNotFoundException("No ship found with id %d".formatted(id)));
        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))
                || Objects.equals(user.getId(), ship.getUser().getId())) {
            return ship;
        } else {
            throw new SecurityException("You don't have authority to access this ship");
        }
    }

}
