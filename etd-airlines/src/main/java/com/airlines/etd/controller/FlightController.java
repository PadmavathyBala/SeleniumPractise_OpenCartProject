package com.airlines.etd.controller;

import com.airlines.etd.dto.CreateFlightRequest;
import com.airlines.etd.dto.FlightDTO;
import com.airlines.etd.dto.UpdateEtdRequest;
import com.airlines.etd.model.Flight;
import com.airlines.etd.service.FlightService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST endpoints for flight CRUD and ETD operations.
 */
@RestController
@RequestMapping("/api/flights")
public class FlightController {

    private final FlightService flightService;

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @GetMapping
    public List<FlightDTO> listAll(@RequestParam(required = false) String search) {
        List<Flight> flights = (search == null || search.isBlank())
                ? flightService.getAllFlights()
                : flightService.searchFlights(search);
        return flights.stream().map(FlightDTO::fromEntity).toList();
    }

    @GetMapping("/{id}")
    public FlightDTO getOne(@PathVariable String id) {
        return FlightDTO.fromEntity(flightService.getFlight(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FlightDTO create(@Valid @RequestBody CreateFlightRequest request) {
        return FlightDTO.fromEntity(flightService.createFlight(request));
    }

    @PutMapping("/{id}/etd")
    public FlightDTO updateEtd(@PathVariable String id,
                               @Valid @RequestBody UpdateEtdRequest request) {
        return FlightDTO.fromEntity(flightService.updateEtd(id, request));
    }

    @PostMapping("/{id}/cancel")
    public FlightDTO cancel(@PathVariable String id,
                            @RequestBody(required = false) Map<String, String> body) {
        String notes = body != null ? body.get("notes") : null;
        return FlightDTO.fromEntity(flightService.cancelFlight(id, notes));
    }

    @PostMapping("/{id}/depart")
    public FlightDTO depart(@PathVariable String id) {
        return FlightDTO.fromEntity(flightService.markDeparted(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        flightService.deleteFlight(id);
        return ResponseEntity.noContent().build();
    }
}
