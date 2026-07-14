package com.airlines.etd.service;

import com.airlines.etd.model.DelayReason;
import com.airlines.etd.model.Flight;
import com.airlines.etd.model.FlightStatus;
import com.airlines.etd.repository.FlightRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

/**
 * Seeds the system with sample flights on startup and simulates real-world
 * updates (status changes, occasional delays) on a schedule.
 */
@Service
public class FlightSimulationService {

    private static final Logger log = LoggerFactory.getLogger(FlightSimulationService.class);
    private final Random random = new Random();

    private final FlightRepository repository;
    private final EtdCalculationService etdService;

    public FlightSimulationService(FlightRepository repository, EtdCalculationService etdService) {
        this.repository = repository;
        this.etdService = etdService;
    }

    @PostConstruct
    public void seedFlights() {
        log.info("Seeding sample flight data...");
        LocalDateTime base = LocalDateTime.now().withSecond(0).withNano(0);

        addFlight("AA101", "American Airlines", "DFW", "LAX", "A12", "Boeing 737-800",
                  base.plusMinutes(15));
        addFlight("DL2234", "Delta Air Lines", "ATL", "JFK", "B7",  "Airbus A320",
                  base.plusMinutes(35));
        addFlight("UA456", "United Airlines",   "ORD", "SFO", "C24", "Boeing 757-200",
                  base.plusMinutes(50));
        addFlight("WN789", "Southwest Airlines","DAL", "PHX", "12",  "Boeing 737 MAX 8",
                  base.plusMinutes(70));
        addFlight("AA512", "American Airlines", "DFW", "MIA", "D17", "Boeing 737-800",
                  base.plusMinutes(90));
        addFlight("UA1108","United Airlines",   "ORD", "DEN", "C12", "Airbus A319",
                  base.plusMinutes(120));
        addFlight("DL891", "Delta Air Lines",   "ATL", "SEA", "B15", "Boeing 757-300",
                  base.plusMinutes(145));
        addFlight("B61234","JetBlue Airways",   "JFK", "BOS", "T5",  "Embraer E190",
                  base.plusMinutes(175));
        addFlight("AS640", "Alaska Airlines",   "SEA", "SAN", "N1",  "Boeing 737-900",
                  base.plusMinutes(200));
        addFlight("F9221", "Frontier",          "DEN", "LAS", "A50", "Airbus A320neo",
                  base.plusMinutes(220));
        addFlight("AA77",  "American Airlines", "DFW", "ORD", "A18", "Boeing 737-800",
                  base.plusMinutes(255));
        addFlight("UA2200","United Airlines",   "EWR", "LAX", "C45", "Boeing 787-9",
                  base.plusMinutes(280));
        addFlight("DL444", "Delta Air Lines",   "ATL", "DTW", "A22", "Boeing 717-200",
                  base.plusMinutes(305));
        addFlight("WN1450","Southwest Airlines","MDW", "BWI", "9",   "Boeing 737-700",
                  base.plusMinutes(330));
        addFlight("AA200", "American Airlines", "DFW", "LHR", "D22", "Boeing 777-300ER",
                  base.plusMinutes(360));

        // Pre-apply a couple of delays for realism
        repository.findByFlightNumber("UA456").ifPresent(f -> {
            etdService.applyDelay(f, DelayReason.WEATHER,
                    "Thunderstorms in SFO area");
            repository.save(f);
        });
        repository.findByFlightNumber("WN789").ifPresent(f -> {
            etdService.applyDelay(f, DelayReason.LATE_INBOUND,
                    "Inbound aircraft delayed from PHX");
            repository.save(f);
        });

        log.info("Seeded {} flights", repository.count());
    }

    private void addFlight(String number, String airline, String origin, String dest,
                           String gate, String aircraft, LocalDateTime scheduled) {
        Flight f = new Flight(null, number, airline, origin, dest, gate, aircraft, scheduled);
        repository.save(f);
    }

    /**
     * Periodic update: refresh flight statuses based on current time, and
     * randomly introduce a delay on one scheduled flight to keep the dashboard live.
     */
    @Scheduled(fixedRate = 30_000) // every 30 seconds
    public void simulateLiveUpdates() {
        LocalDateTime now = LocalDateTime.now();
        List<Flight> flights = repository.findAll();

        for (Flight f : flights) {
            FlightStatus computed = etdService.computeStatus(f, now);
            if (computed != f.getStatus()) {
                log.debug("Status change for {}: {} -> {}",
                          f.getFlightNumber(), f.getStatus(), computed);
                f.setStatus(computed);
                repository.save(f);
            }
        }

        // 20% chance per cycle: introduce a delay on a random scheduled flight
        if (random.nextInt(100) < 20) {
            List<Flight> scheduled = flights.stream()
                    .filter(f -> f.getStatus() == FlightStatus.SCHEDULED)
                    .filter(f -> !f.isDelayed())
                    .toList();
            if (!scheduled.isEmpty()) {
                Flight victim = scheduled.get(random.nextInt(scheduled.size()));
                DelayReason reason = DelayReason.values()[
                        random.nextInt(DelayReason.values().length)];
                etdService.applyDelay(victim, reason, "Auto-simulated delay");
                repository.save(victim);
                log.info("Simulated delay on {}: {} ({} min)",
                         victim.getFlightNumber(), reason,
                         etdService.estimateAdditionalDelay(reason));
            }
        }
    }
}
