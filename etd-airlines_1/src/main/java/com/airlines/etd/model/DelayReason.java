package com.airlines.etd.model;

/**
 * Categorized reasons for flight delays, used for analytics and ETD calculations.
 */
public enum DelayReason {
    WEATHER("Weather"),
    AIR_TRAFFIC("Air Traffic Control"),
    MECHANICAL("Mechanical Issue"),
    CREW("Crew Availability"),
    SECURITY("Security"),
    LATE_INBOUND("Late Arriving Aircraft"),
    FUELING("Fueling Delay"),
    CATERING("Catering"),
    BAGGAGE("Baggage Handling"),
    OTHER("Other");

    private final String displayName;

    DelayReason(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
