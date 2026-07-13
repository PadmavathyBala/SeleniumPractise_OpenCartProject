package com.airlines.etd.service;

import com.airlines.etd.model.DelayReason;
import com.airlines.etd.model.Flight;
import com.airlines.etd.model.FlightStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Encapsulates ETD calculation rules.
 * In a real airline system this would integrate with weather, ATC, and aircraft tracking feeds.
 */
@Service
public class EtdCalculationService {

    /**
     * Recomputes status of a flight based on current time and ETD.
     */
    public FlightStatus computeStatus(Flight flight, LocalDateTime now) {
        if (flight.getStatus() == FlightStatus.CANCELLED
                || flight.getStatus() == FlightStatus.DIVERTED
                || flight.getStatus() == FlightStatus.DEPARTED) {
            return flight.getStatus();
        }

        LocalDateTime etd = flight.getEstimatedDeparture();
        if (etd == null) return FlightStatus.SCHEDULED;

        // Already past ETD — mark as departed
        if (now.isAfter(etd)) {
            return FlightStatus.DEPARTED;
        }

        // Within 30 min of departure: boarding
        if (now.plusMinutes(30).isAfter(etd) || now.plusMinutes(30).isEqual(etd)) {
            return flight.isDelayed() ? FlightStatus.DELAYED : FlightStatus.BOARDING;
        }

        return flight.isDelayed() ? FlightStatus.DELAYED : FlightStatus.SCHEDULED;
    }

    /**
     * Estimates additional delay (minutes) given a reason. Production systems would
     * pull from operational data; we use reasonable static averages.
     */
    public int estimateAdditionalDelay(DelayReason reason) {
        if (reason == null) return 0;
        return switch (reason) {
            case WEATHER       -> 45;
            case AIR_TRAFFIC   -> 20;
            case MECHANICAL    -> 60;
            case CREW          -> 30;
            case SECURITY      -> 25;
            case LATE_INBOUND  -> 35;
            case FUELING       -> 15;
            case CATERING      -> 10;
            case BAGGAGE       -> 15;
            case OTHER         -> 20;
        };
    }

    /**
     * Applies a delay to a flight: bumps ETD forward by the delay's estimated impact.
     */
    public void applyDelay(Flight flight, DelayReason reason, String notes) {
        int additionalMinutes = estimateAdditionalDelay(reason);
        LocalDateTime newEtd = flight.getEstimatedDeparture().plusMinutes(additionalMinutes);
        flight.setEstimatedDeparture(newEtd);
        flight.setDelayReason(reason);
        flight.setDelayNotes(notes);
        flight.setStatus(FlightStatus.DELAYED);
    }
}
