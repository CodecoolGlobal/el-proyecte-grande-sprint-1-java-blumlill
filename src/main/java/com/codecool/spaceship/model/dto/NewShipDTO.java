package com.codecool.spaceship.model.dto;

import com.codecool.spaceship.model.ship.ShipType;
import com.codecool.spaceship.model.ship.shipparts.Color;

public record NewShipDTO(String name, Color color, ShipType type) {
}
