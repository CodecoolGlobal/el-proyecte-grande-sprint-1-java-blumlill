package com.codecool.spaceship.model.mission;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime endTime;
    private EventType eventType;
    private String eventMessage;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return ((endTime != null && event.endTime != null
                    && (ChronoUnit.SECONDS.between(endTime, event.endTime) < 0.5
                        && ChronoUnit.SECONDS.between(endTime, event.endTime) > -0.5))
                    || (endTime == null && event.endTime == null))
                && eventType == event.eventType
                && Objects.equals(eventMessage, event.eventMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(endTime, eventType, eventMessage);
    }
}
