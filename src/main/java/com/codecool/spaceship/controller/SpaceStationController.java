package com.codecool.spaceship.controller;

import com.codecool.spaceship.model.Resource;
import com.codecool.spaceship.model.dto.HangarDTO;
import com.codecool.spaceship.model.dto.SpaceStationDTO;
import com.codecool.spaceship.model.dto.SpaceStationStorageDTO;
import com.codecool.spaceship.model.exception.StorageException;
import com.codecool.spaceship.model.exception.UpgradeNotAvailableException;
import com.codecool.spaceship.model.ship.MinerShip;
import com.codecool.spaceship.model.ship.shipparts.Color;
import com.codecool.spaceship.service.BaseService;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/base")
public class SpaceStationController {

    private final BaseService baseService;

    @Autowired
    public SpaceStationController(BaseService baseService) {
        this.baseService = baseService;
    }

    @GetMapping()
    public ResponseEntity<SpaceStationDTO> getBase() {
        return ResponseEntity.ok(new SpaceStationDTO(baseService.getBase()));
    }

    @GetMapping("/storage")
    public ResponseEntity<SpaceStationStorageDTO> getStorage() {
        return ResponseEntity.ok(new SpaceStationStorageDTO(baseService.getBase().getStorage()));
    }
    @GetMapping("/storage/upgrade")
    public ResponseEntity<Map<Resource,Integer>> getStorageUpgradeCost() throws UpgradeNotAvailableException {
        return ResponseEntity.ok(baseService.getBase().getStorage().getUpgradeCost());
    }

    @GetMapping("/storage/resources")
    public ResponseEntity<Map<Resource,Integer>> getStorageResources() {
        return ResponseEntity.ok(baseService.getBase().getStorage().getStoredItems());
    }
    @GetMapping("/hangar")
    public ResponseEntity<HangarDTO> getHangar() {
        return ResponseEntity.ok(new HangarDTO(baseService.getBase().getHangar()));
    }

    @GetMapping("/hangar/upgrade")
    public ResponseEntity<Map<Resource,Integer>> getHangarUpgradeCost() throws UpgradeNotAvailableException {
        return ResponseEntity.ok(baseService.getBase().getHangar().getUpgradeCost());
    }

    @PostMapping("/add/resources")
    public ResponseEntity<Boolean> addResource(@RequestBody Map<Resource, Integer> resources) {
        for (Resource resource : resources.keySet()) {
            try {
                baseService.addResource(resource, resources.get(resource));
            } catch (StorageException e) {
                return ResponseEntity.of(ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(400), e.getMessage())).build();
            }
        }
        return ResponseEntity.ok(true);
    }

    @PostMapping("/hangar/add")
    public ResponseEntity<Boolean> addShip(@RequestBody ObjectNode objectNode) {
        try {
            return ResponseEntity.ok(baseService.addShip(new MinerShip(objectNode.get("name").asText(), Color.valueOf(objectNode.get("color").asText()))));
        } catch (Exception e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(400), e.getMessage())).build();
        }
    }

    @PostMapping("/upgrade/storage")
    public ResponseEntity<Boolean> upgradeStorage() {
        try {
            return ResponseEntity.ok(baseService.upgradeStorage());
        } catch (Exception e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(400), e.getMessage())).build();
        }
    }

    @PostMapping("/upgrade/hangar")
    public ResponseEntity<Boolean> upgradeHangar() {
        try {
            return ResponseEntity.ok(baseService.upgradeHangar());
        } catch (Exception e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(400), e.getMessage())).build();
        }
    }
}
