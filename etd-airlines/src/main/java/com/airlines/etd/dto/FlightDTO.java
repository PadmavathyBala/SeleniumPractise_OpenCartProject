package com.airlines.etd.dto;

import com.airlines.etd.model.DelayReason;
import com.airlines.etd.model.Flight;
import com.airlines.etd.model.FlightStatus;

import java.time.LocalDateTime;

/**
 * Data Transfer Object exposed via REST.
 * Includes a computed delay field for the UI.
 */
public class FlightDTO {

    private String id;
    private String flightNumber;
    private String airline;
    private String origin;
    private String destination;
    private String gate;
    private String aircraftType;
    private LocalDateTime scheduledDeparture;
    private LocalDateTime estimatedDeparture;
    private LocalDateTime actualDeparture;
    private FlightStatus status;
    private String statusDisplay;
    private DelayReason delayReason;
    private String delayReasonDisplay;
    private String delayNotes;
    private long delayMinutes;
    private boolean delayed;
    private LocalDateTime lastUpdated;

    public static FlightDTO fromEntity(Flight f) {
        FlightDTO dto = new FlightDTO();
        dto.id = f.getId();
        dto.flightNumber = f.getFlightNumber();
        dto.airline = f.getAirline();
        dto.origin = f.getOrigin();
        dto.destination = f.getDestination();
        dto.gate = f.getGate();
        dto.aircraftType = f.getAircraftType();
        dto.scheduledDeparture = f.getScheduledDeparture();
        dto.estimatedDeparture = f.getEstimatedDeparture();
        dto.actualDeparture = f.getActualDeparture();
        dto.status = f.getStatus();
        dto.statusDisplay = f.getStatus() != null ? f.getStatus().getDisplayName() : null;
        dto.delayReason = f.getDelayReason();
        dto.delayReasonDisplay = f.getDelayReason() != null ? f.getDelayReason().getDisplayName() : null;
        dto.delayNotes = f.getDelayNotes();
        dto.delayMinutes = f.getDelayMinutes();
        dto.delayed = f.isDelayed();
        dto.lastUpdated = f.getLastUpdated();
        return dto;
    }

    // getters
    public String getId() { return id; }
    public String getFlightNumber() { return flightNumber; }
    public String getAirline() { return airline; }
    public String getOrigin() { return origin; }
    public String getDestination() { return destination; }
    public String getGate() { return gate; }
    public String getAircraftType() { return aircraftType; }
    public LocalDateTime getScheduledDeparture() { return scheduledDeparture; }
    public LocalDateTime getEstimatedDeparture() { return estimatedDeparture; }
    public LocalDateTime getActualDeparture() { return actualDeparture; }
    public FlightStatus getStatus() { return status; }
    public String getStatusDisplay() { return statusDisplay; }
    public DelayReason getDelayReason() { return delayReason; }
    public String getDelayReasonDisplay() { return delayReasonDisplay; }
    public String getDelayNotes() { return delayNotes; }
    public long getDelayMinutes() { return delayMinutes; }
    public boolean isDelayed() { return delayed; }
    public LocalDateTime getLastUpdated() { return lastUpdated; }
}
