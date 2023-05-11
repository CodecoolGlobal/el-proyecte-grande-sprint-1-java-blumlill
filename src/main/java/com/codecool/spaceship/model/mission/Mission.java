package com.codecool.spaceship.model.mission;

import com.codecool.spaceship.model.Location;
import com.codecool.spaceship.model.ship.SpaceShip;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Mission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime currentObjectiveTime;
    private LocalDateTime approxEndTime;
    private MissionStatus currentStatus;
    private MissionType missionType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location location;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "ship_id")
    private SpaceShip ship;
    private long travelDurationInSecs;
    private long activityDurationInSecs;
    @OneToMany(cascade = CascadeType.MERGE)
    @JoinColumn(name = "mission_id")
    private List<Event> events;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mission mission = (Mission) o;
        return travelDurationInSecs == mission.travelDurationInSecs
                && activityDurationInSecs == mission.activityDurationInSecs
                && ((startTime != null && mission.startTime != null
                    && (ChronoUnit.SECONDS.between(startTime, mission.startTime) < 0.5
                        && ChronoUnit.SECONDS.between(startTime, mission.startTime) > -0.5))
                    || startTime == null && mission.startTime == null)
                && ((currentObjectiveTime != null && mission.currentObjectiveTime != null
                    && (ChronoUnit.SECONDS.between(currentObjectiveTime, mission.currentObjectiveTime) < 0.5
                        && ChronoUnit.SECONDS.between(currentObjectiveTime, mission.currentObjectiveTime) > -0.5))
                    || currentObjectiveTime == null && mission.currentObjectiveTime == null)
                && ((approxEndTime != null && mission.approxEndTime != null
                && (ChronoUnit.SECONDS.between(approxEndTime, mission.approxEndTime) < 0.5
                && ChronoUnit.SECONDS.between(approxEndTime, mission.approxEndTime) > -0.5))
                || approxEndTime == null && mission.approxEndTime == null)
                && currentStatus == mission.currentStatus
                && missionType == mission.missionType
                && Objects.equals(location, mission.location)
                && Objects.equals(ship, mission.ship)
                && Objects.equals(events, mission.events);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startTime, currentObjectiveTime, approxEndTime, currentStatus, missionType, location, ship, travelDurationInSecs, activityDurationInSecs, events);
    }
}