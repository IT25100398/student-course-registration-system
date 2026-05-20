package com.example.courseregistration.dto;

import lombok.Data;

@Data
public class CourseDTO {
    private Long id;
    private String courseCode;
    private String courseName;
    private Integer credits;
    private String department;
    private String instructor;
    private Integer capacity;
    private Integer enrolledCount;
    private String schedule;
    private String room;
    private String semester;
    private Integer year;
    private String status;
    private boolean available;
    private int availableSeats;
}