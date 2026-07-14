package com.airlines.etd.dto;

import com.airlines.etd.model.DelayReason;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

/**
 * Request payload for updating the estimated departure time of a flight.
 */
public class UpdateEtdRequest {

    @NotNull(message = "New estimated departure time is required")
    private LocalDateTime newEstimatedDeparture;

    private DelayReason delayReason;
    private String delayNotes;

    public UpdateEtdRequest() {}

    public LocalDateTime getNewEstimatedDeparture() { return newEstimatedDeparture; }
    public void setNewEstimatedDeparture(LocalDateTime t) { this.newEstimatedDeparture = t; }

    public DelayReason getDelayReason() { return delayReason; }
    public void setDelayReason(DelayReason r) { this.delayReason = r; }

    public String getDelayNotes() { return delayNotes; }
    public void setDelayNotes(String n) { this.delayNotes = n; }
}
