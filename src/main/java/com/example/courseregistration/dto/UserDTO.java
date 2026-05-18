package com.example.courseregistration.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String password;
    private String fullName;
    private String role;
    private String phone;
    private String status;
    private LocalDateTime lastLogin;
    private LocalDateTime createdAt;
}