package com.airlines.etd.dto;

import com.airlines.etd.model.DelayReason;
import com.airlines.etd.model.FlightStatus;

import java.util.Map;

/**
 * Aggregated statistics for the dashboard.
 */
public class StatisticsDTO {

    private long totalFlights;
    private long onTimeCount;
    private long delayedCount;
    private long cancelledCount;
    private double onTimePercentage;
    private double averageDelayMinutes;
    private Map<FlightStatus, Long> countByStatus;
    private Map<DelayReason, Long> countByDelayReason;
    private Map<String, Long> countByAirline;

    public long getTotalFlights() { return totalFlights; }
    public void setTotalFlights(long v) { this.totalFlights = v; }

    public long getOnTimeCount() { return onTimeCount; }
    public void setOnTimeCount(long v) { this.onTimeCount = v; }

    public long getDelayedCount() { return delayedCount; }
    public void setDelayedCount(long v) { this.delayedCount = v; }

    public long getCancelledCount() { return cancelledCount; }
    public void setCancelledCount(long v) { this.cancelledCount = v; }

    public double getOnTimePercentage() { return onTimePercentage; }
    public void setOnTimePercentage(double v) { this.onTimePercentage = v; }

    public double getAverageDelayMinutes() { return averageDelayMinutes; }
    public void setAverageDelayMinutes(double v) { this.averageDelayMinutes = v; }

    public Map<FlightStatus, Long> getCountByStatus() { return countByStatus; }
    public void setCountByStatus(Map<FlightStatus, Long> v) { this.countByStatus = v; }

    public Map<DelayReason, Long> getCountByDelayReason() { return countByDelayReason; }
    public void setCountByDelayReason(Map<DelayReason, Long> v) { this.countByDelayReason = v; }

    public Map<String, Long> getCountByAirline() { return countByAirline; }
    public void setCountByAirline(Map<String, Long> v) { this.countByAirline = v; }
}
