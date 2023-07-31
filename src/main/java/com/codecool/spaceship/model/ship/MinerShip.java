package com.codecool.spaceship.model.ship;

import com.codecool.spaceship.model.resource.ShipResource;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class MinerShip extends SpaceShip {
    private int drillLevel;
    private int storageLevel;
    @OneToMany(cascade = CascadeType.MERGE)
    @JoinColumn(name ="ship_id")
    private Set<ShipResource> resources;
}
