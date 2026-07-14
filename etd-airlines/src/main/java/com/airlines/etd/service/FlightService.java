package com.airlines.etd.service;

import com.airlines.etd.dto.CreateFlightRequest;
import com.airlines.etd.dto.UpdateEtdRequest;
import com.airlines.etd.exception.FlightNotFoundException;
import com.airlines.etd.model.Flight;
import com.airlines.etd.model.FlightStatus;
import com.airlines.etd.repository.FlightRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Core orchestration service for flight operations.
 */
@Service
public class FlightService {

    private final FlightRepository repository;
    private final EtdCalculationService etdService;

    public FlightService(FlightRepository repository, EtdCalculationService etdService) {
        this.repository = repository;
        this.etdService = etdService;
    }

    public List<Flight> getAllFlights() {
        return repository.findAll().stream()
                .sorted(Comparator.comparing(Flight::getScheduledDeparture,
                        Comparator.nullsLast(Comparator.naturalOrder())))
                .collect(Collectors.toList());
    }

    public Flight getFlight(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new FlightNotFoundException(id));
    }

    public Flight createFlight(CreateFlightRequest request) {
        // Reject duplicates by flight number for the same scheduled time
        repository.findByFlightNumber(request.getFlightNumber()).ifPresent(existing -> {
            if (existing.getScheduledDeparture().equals(request.getScheduledDeparture())) {
                throw new IllegalArgumentException(
                        "Flight " + request.getFlightNumber() +
                        " already exists for this scheduled time");
            }
        });

        Flight f = new Flight();
        f.setFlightNumber(request.getFlightNumber());
        f.setAirline(request.getAirline());
        f.setOrigin(request.getOrigin());
        f.setDestination(request.getDestination());
        f.setGate(request.getGate());
        f.setAircraftType(request.getAircraftType());
        f.setScheduledDeparture(request.getScheduledDeparture());
        f.setEstimatedDeparture(request.getScheduledDeparture());
        f.setStatus(FlightStatus.SCHEDULED);
        return repository.save(f);
    }

    public Flight updateEtd(String id, UpdateEtdRequest req) {
        Flight f = getFlight(id);

        if (req.getNewEstimatedDeparture().isBefore(LocalDateTime.now().minusMinutes(5))) {
            throw new IllegalArgumentException(
                    "New ETD cannot be more than 5 minutes in the past");
        }

        f.setEstimatedDeparture(req.getNewEstimatedDeparture());
        if (req.getDelayReason() != null) {
            f.setDelayReason(req.getDelayReason());
        }
        if (req.getDelayNotes() != null) {
            f.setDelayNotes(req.getDelayNotes());
        }

        // Auto-update status based on the new ETD
        FlightStatus newStatus = etdService.computeStatus(f, LocalDateTime.now());
        f.setStatus(newStatus);

        return repository.save(f);
    }

    public Flight cancelFlight(String id, String notes) {
        Flight f = getFlight(id);
        f.setStatus(FlightStatus.CANCELLED);
        f.setDelayNotes(notes);
        return repository.save(f);
    }

    public Flight markDeparted(String id) {
        Flight f = getFlight(id);
        f.setStatus(FlightStatus.DEPARTED);
        f.setActualDeparture(LocalDateTime.now());
        return repository.save(f);
    }

    public void deleteFlight(String id) {
        if (!repository.findById(id).isPresent()) {
            throw new FlightNotFoundException(id);
        }
        repository.deleteById(id);
    }

    public List<Flight> searchFlights(String query) {
        if (query == null || query.isBlank()) {
            return getAllFlights();
        }
        String q = query.toLowerCase();
        return repository.findAll().stream()
                .filter(f ->
                        f.getFlightNumber().toLowerCase().contains(q) ||
                        f.getOrigin().toLowerCase().contains(q) ||
                        f.getDestination().toLowerCase().contains(q) ||
                        (f.getAirline() != null && f.getAirline().toLowerCase().contains(q)))
                .sorted(Comparator.comparing(Flight::getScheduledDeparture,
                        Comparator.nullsLast(Comparator.naturalOrder())))
                .collect(Collectors.toList());
    }
}
