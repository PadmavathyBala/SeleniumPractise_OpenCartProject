package com.airlines.etd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main entry point for the ETD Airlines application.
 * Estimated Time of Departure management system for airlines.
 */
@SpringBootApplication
@EnableScheduling
public class EtdApplication {

    public static void main(String[] args) {
        SpringApplication.run(EtdApplication.class, args);
    }
}
