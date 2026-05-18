package com.example.courseregistration.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class RegistrationDTO {
    private Long id;
    private Long studentId;
    private String studentName;
    private Long courseId;
    private String courseName;
    private String courseCode;
    private LocalDate registrationDate;
    private String grade;
    private String status;
}