package com.example.courseregistration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CourseRegistrationApplication {
    public static void main(String[] args) {
        SpringApplication.run(CourseRegistrationApplication.class, args);
        System.out.println("========================================");
        System.out.println(" Course Registration System Started!");
        System.out.println(" Access at: http://localhost:8080");
        System.out.println("========================================");
    }
}