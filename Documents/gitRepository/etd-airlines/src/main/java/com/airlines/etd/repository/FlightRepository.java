package com.airlines.etd.repository;

import com.airlines.etd.model.Flight;
import com.airlines.etd.model.FlightStatus;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Thread-safe in-memory repository for flights.
 * Replace with a JPA repository for production persistence.
 */
@Repository
public class FlightRepository {

    private final Map<String, Flight> flights = new ConcurrentHashMap<>();

    public Flight save(Flight flight) {
        if (flight.getId() == null) {
            flight.setId(UUID.randomUUID().toString());
        }
        flights.put(flight.getId(), flight);
        return flight;
    }

    public Optional<Flight> findById(String id) {
        return Optional.ofNullable(flights.get(id));
    }

    public Optional<Flight> findByFlightNumber(String flightNumber) {
        return flights.values().stream()
                .filter(f -> f.getFlightNumber().equalsIgnoreCase(flightNumber))
                .findFirst();
    }

    public List<Flight> findAll() {
        return new ArrayList<>(flights.values());
    }

    public List<Flight> findByStatus(FlightStatus status) {
        return flights.values().stream()
                .filter(f -> f.getStatus() == status)
                .collect(Collectors.toList());
    }

    public List<Flight> findByOrigin(String origin) {
        return flights.values().stream()
                .filter(f -> f.getOrigin().equalsIgnoreCase(origin))
                .collect(Collectors.toList());
    }

    public List<Flight> findByDestination(String destination) {
        return flights.values().stream()
                .filter(f -> f.getDestination().equalsIgnoreCase(destination))
                .collect(Collectors.toList());
    }

    public void deleteById(String id) {
        flights.remove(id);
    }

    public long count() {
        return flights.size();
    }

    public void deleteAll() {
        flights.clear();
    }
}
