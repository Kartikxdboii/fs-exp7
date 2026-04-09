package com.example.exp7;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Exp7Application {

    public static void main(String[] args) {
        SpringApplication.run(Exp7Application.class, args);
        System.out.println("\n========================================");
        System.out.println("  Experiment 7 - Spring Security + JWT");
        System.out.println("  Server running on http://localhost:8080");
        System.out.println("========================================\n");
    }
}
