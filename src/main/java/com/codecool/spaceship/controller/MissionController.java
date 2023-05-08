package com.codecool.spaceship.controller;

import com.codecool.spaceship.model.dto.MissionDTO;
import com.codecool.spaceship.model.exception.DataNotFoundException;
import com.codecool.spaceship.service.MissionService;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mission")
public class MissionController {

    private final MissionService missionService;

    @Autowired
    public MissionController(MissionService missionService) {
        this.missionService = missionService;
    }

    @GetMapping("/active")
    public ResponseEntity<List<MissionDTO>> getAllActiveMissions() {
        return ResponseEntity.ok(missionService.getAllActiveMissions());
    }

    @PostMapping("/")
    public ResponseEntity<MissionDTO> startNewMission(@RequestBody ObjectNode objectNode) {
        try {
            return ResponseEntity.ok(missionService.startNewMission(
                    objectNode.get("shipId").asLong(),
                    objectNode.get("locationId").asLong(),
                    objectNode.get("activityDuration").asLong()
            ));
        } catch (DataNotFoundException e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(404), e.getMessage())).build();
        }
    }
}
