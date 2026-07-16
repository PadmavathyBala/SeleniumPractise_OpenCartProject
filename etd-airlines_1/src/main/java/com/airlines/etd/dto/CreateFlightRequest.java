package com.airlines.etd.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDateTime;

/**
 * Request payload for creating a new flight.
 */
public class CreateFlightRequest {

    @NotBlank(message = "Flight number is required")
    @Pattern(regexp = "^[A-Z]{2,3}\\d{1,4}$",
             message = "Flight number must be like 'AA123' or 'UAL1234'")
    private String flightNumber;

    @NotBlank(message = "Airline is required")
    private String airline;

    @NotBlank(message = "Origin is required")
    @Pattern(regexp = "^[A-Z]{3}$", message = "Origin must be a 3-letter IATA code")
    private String origin;

    @NotBlank(message = "Destination is required")
    @Pattern(regexp = "^[A-Z]{3}$", message = "Destination must be a 3-letter IATA code")
    private String destination;

    private String gate;
    private String aircraftType;

    @NotNull(message = "Scheduled departure is required")
    private LocalDateTime scheduledDeparture;

    public String getFlightNumber() { return flightNumber; }
    public void setFlightNumber(String s) { this.flightNumber = s; }

    public String getAirline() { return airline; }
    public void setAirline(String s) { this.airline = s; }

    public String getOrigin() { return origin; }
    public void setOrigin(String s) { this.origin = s; }

    public String getDestination() { return destination; }
    public void setDestination(String s) { this.destination = s; }

    public String getGate() { return gate; }
    public void setGate(String s) { this.gate = s; }

    public String getAircraftType() { return aircraftType; }
    public void setAircraftType(String s) { this.aircraftType = s; }

    public LocalDateTime getScheduledDeparture() { return scheduledDeparture; }
    public void setScheduledDeparture(LocalDateTime t) { this.scheduledDeparture = t; }
}
