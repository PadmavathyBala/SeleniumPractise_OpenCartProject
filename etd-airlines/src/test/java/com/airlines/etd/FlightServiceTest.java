package com.airlines.etd;

import com.airlines.etd.dto.CreateFlightRequest;
import com.airlines.etd.dto.UpdateEtdRequest;
import com.airlines.etd.model.DelayReason;
import com.airlines.etd.model.Flight;
import com.airlines.etd.repository.FlightRepository;
import com.airlines.etd.service.EtdCalculationService;
import com.airlines.etd.service.FlightService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class FlightServiceTest {

    private FlightService service;
    private FlightRepository repository;
    private EtdCalculationService etdService;

    @BeforeEach
    void setUp() {
        repository = new FlightRepository();
        etdService = new EtdCalculationService();
        service = new FlightService(repository, etdService);
    }

    @Test
    void createFlightAssignsIdAndDefaults() {
        CreateFlightRequest req = new CreateFlightRequest();
        req.setFlightNumber("AA999");
        req.setAirline("American Airlines");
        req.setOrigin("DFW");
        req.setDestination("LAX");
        req.setGate("A12");
        req.setAircraftType("Boeing 737");
        req.setScheduledDeparture(LocalDateTime.now().plusHours(2));

        Flight created = service.createFlight(req);

        assertNotNull(created.getId());
        assertEquals("AA999", created.getFlightNumber());
        assertEquals(created.getScheduledDeparture(), created.getEstimatedDeparture());
        assertEquals(0, created.getDelayMinutes());
    }

    @Test
    void updateEtdMarksFlightAsDelayed() {
        CreateFlightRequest req = new CreateFlightRequest();
        req.setFlightNumber("AA111");
        req.setAirline("American Airlines");
        req.setOrigin("DFW");
        req.setDestination("LAX");
        req.setScheduledDeparture(LocalDateTime.now().plusHours(2));
        Flight created = service.createFlight(req);

        UpdateEtdRequest update = new UpdateEtdRequest();
        update.setNewEstimatedDeparture(created.getScheduledDeparture().plusMinutes(45));
        update.setDelayReason(DelayReason.WEATHER);
        update.setDelayNotes("Thunderstorms");

        Flight updated = service.updateEtd(created.getId(), update);

        assertEquals(45, updated.getDelayMinutes());
        assertTrue(updated.isDelayed());
        assertEquals(DelayReason.WEATHER, updated.getDelayReason());
    }
}
