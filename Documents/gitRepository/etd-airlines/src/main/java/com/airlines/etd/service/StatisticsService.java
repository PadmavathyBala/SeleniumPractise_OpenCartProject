package com.airlines.etd.service;

import com.airlines.etd.dto.StatisticsDTO;
import com.airlines.etd.model.DelayReason;
import com.airlines.etd.model.Flight;
import com.airlines.etd.model.FlightStatus;
import com.airlines.etd.repository.FlightRepository;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Aggregates flight data into dashboard statistics.
 */
@Service
public class StatisticsService {

    private final FlightRepository repository;

    public StatisticsService(FlightRepository repository) {
        this.repository = repository;
    }

    public StatisticsDTO buildStatistics() {
        List<Flight> all = repository.findAll();
        StatisticsDTO dto = new StatisticsDTO();

        dto.setTotalFlights(all.size());

        long delayed = all.stream().filter(Flight::isDelayed).count();
        long cancelled = all.stream()
                .filter(f -> f.getStatus() == FlightStatus.CANCELLED)
                .count();
        long onTime = all.stream()
                .filter(f -> !f.isDelayed())
                .filter(f -> f.getStatus() != FlightStatus.CANCELLED)
                .count();

        dto.setDelayedCount(delayed);
        dto.setCancelledCount(cancelled);
        dto.setOnTimeCount(onTime);
        dto.setOnTimePercentage(all.isEmpty() ? 0
                : (onTime * 100.0) / all.size());

        double avgDelay = all.stream()
                .filter(Flight::isDelayed)
                .mapToLong(Flight::getDelayMinutes)
                .average()
                .orElse(0);
        dto.setAverageDelayMinutes(Math.round(avgDelay * 10.0) / 10.0);

        // Count by status
        Map<FlightStatus, Long> byStatus = new EnumMap<>(FlightStatus.class);
        for (FlightStatus s : FlightStatus.values()) {
            byStatus.put(s, 0L);
        }
        for (Flight f : all) {
            byStatus.merge(f.getStatus(), 1L, Long::sum);
        }
        dto.setCountByStatus(byStatus);

        // Count by delay reason (only flights with a reason)
        Map<DelayReason, Long> byReason = all.stream()
                .filter(f -> f.getDelayReason() != null)
                .collect(Collectors.groupingBy(
                        Flight::getDelayReason,
                        () -> new EnumMap<>(DelayReason.class),
                        Collectors.counting()));
        dto.setCountByDelayReason(byReason);

        // Count by airline
        Map<String, Long> byAirline = all.stream()
                .filter(f -> f.getAirline() != null)
                .collect(Collectors.groupingBy(
                        Flight::getAirline,
                        TreeMap::new,
                        Collectors.counting()));
        dto.setCountByAirline(byAirline);

        return dto;
    }
}
