package com.agriserve;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * AgriServe Application Entry Point.
 * Agricultural Extension & Farmer Advisory System.
 */
@SpringBootApplication
@EnableAsync
public class AgriServeApplication {

    public static void main(String[] args) {
        SpringApplication.run(AgriServeApplication.class, args);
    }
}
