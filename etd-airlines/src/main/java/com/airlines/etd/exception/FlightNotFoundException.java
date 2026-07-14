package com.airlines.etd.exception;

/**
 * Thrown when a flight cannot be located by id or flight number.
 */
public class FlightNotFoundException extends RuntimeException {

    public FlightNotFoundException(String id) {
        super("Flight not found: " + id);
    }
}
