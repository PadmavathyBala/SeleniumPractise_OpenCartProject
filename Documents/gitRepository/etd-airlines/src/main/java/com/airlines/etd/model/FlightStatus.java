package com.airlines.etd.model;

/**
 * Represents the current operational status of a flight.
 */
public enum FlightStatus {
    SCHEDULED("Scheduled"),
    BOARDING("Boarding"),
    DEPARTED("Departed"),
    DELAYED("Delayed"),
    CANCELLED("Cancelled"),
    DIVERTED("Diverted");

    private final String displayName;

    FlightStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
