package com.airlines.etd.model;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

/**
 * Represents a single flight with all relevant departure information.
 */
public class Flight {

    private String id;
    private String flightNumber;
    private String airline;
    private String origin;          // IATA code, e.g. "DFW"
    private String destination;     // IATA code, e.g. "LAX"
    private String gate;
    private String aircraftType;
    private LocalDateTime scheduledDeparture;
    private LocalDateTime estimatedDeparture;
    private LocalDateTime actualDeparture;
    private FlightStatus status;
    private DelayReason delayReason;
    private String delayNotes;
    private LocalDateTime lastUpdated;

    public Flight() {
        this.lastUpdated = LocalDateTime.now();
    }

    public Flight(String id, String flightNumber, String airline, String origin,
                  String destination, String gate, String aircraftType,
                  LocalDateTime scheduledDeparture) {
        this();
        this.id = id;
        this.flightNumber = flightNumber;
        this.airline = airline;
        this.origin = origin;
        this.destination = destination;
        this.gate = gate;
        this.aircraftType = aircraftType;
        this.scheduledDeparture = scheduledDeparture;
        this.estimatedDeparture = scheduledDeparture;
        this.status = FlightStatus.SCHEDULED;
    }

    /**
     * Calculates the delay in minutes between scheduled and estimated departure.
     * Returns 0 if estimated is earlier than or equal to scheduled.
     */
    public long getDelayMinutes() {
        if (scheduledDeparture == null || estimatedDeparture == null) {
            return 0;
        }
        long minutes = ChronoUnit.MINUTES.between(scheduledDeparture, estimatedDeparture);
        return Math.max(0, minutes);
    }

    /**
     * Returns true if the flight is considered significantly delayed (15+ min).
     */
    public boolean isDelayed() {
        return getDelayMinutes() >= 15;
    }

    // ---------- getters / setters ----------

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getFlightNumber() { return flightNumber; }
    public void setFlightNumber(String flightNumber) { this.flightNumber = flightNumber; }

    public String getAirline() { return airline; }
    public void setAirline(String airline) { this.airline = airline; }

    public String getOrigin() { return origin; }
    public void setOrigin(String origin) { this.origin = origin; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public String getGate() { return gate; }
    public void setGate(String gate) { this.gate = gate; }

    public String getAircraftType() { return aircraftType; }
    public void setAircraftType(String aircraftType) { this.aircraftType = aircraftType; }

    public LocalDateTime getScheduledDeparture() { return scheduledDeparture; }
    public void setScheduledDeparture(LocalDateTime scheduledDeparture) {
        this.scheduledDeparture = scheduledDeparture;
    }

    public LocalDateTime getEstimatedDeparture() { return estimatedDeparture; }
    public void setEstimatedDeparture(LocalDateTime estimatedDeparture) {
        this.estimatedDeparture = estimatedDeparture;
        this.lastUpdated = LocalDateTime.now();
    }

    public LocalDateTime getActualDeparture() { return actualDeparture; }
    public void setActualDeparture(LocalDateTime actualDeparture) {
        this.actualDeparture = actualDeparture;
    }

    public FlightStatus getStatus() { return status; }
    public void setStatus(FlightStatus status) {
        this.status = status;
        this.lastUpdated = LocalDateTime.now();
    }

    public DelayReason getDelayReason() { return delayReason; }
    public void setDelayReason(DelayReason delayReason) { this.delayReason = delayReason; }

    public String getDelayNotes() { return delayNotes; }
    public void setDelayNotes(String delayNotes) { this.delayNotes = delayNotes; }

    public LocalDateTime getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Flight)) return false;
        Flight flight = (Flight) o;
        return Objects.equals(id, flight.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
