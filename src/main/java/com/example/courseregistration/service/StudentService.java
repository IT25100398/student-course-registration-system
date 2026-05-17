package com.example.courseregistration.service;

import com.example.courseregistration.dto.StudentDTO;
import com.example.courseregistration.model.Student;
import com.example.courseregistration.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;

    public List<StudentDTO> getAllStudents() {
        return studentRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public StudentDTO getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        return convertToDTO(student);
    }

    public StudentDTO createStudent(StudentDTO studentDTO) {
        if (studentRepository.existsByStudentId(studentDTO.getStudentId())) {
            throw new RuntimeException("Student ID already exists");
        }
        if (studentRepository.existsByEmail(studentDTO.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        Student student = convertToEntity(studentDTO);
        student.setEnrollmentDate(LocalDate.now());
        student = studentRepository.save(student);
        return convertToDTO(student);
    }

    public StudentDTO updateStudent(Long id, StudentDTO studentDTO) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        student.setFirstName(studentDTO.getFirstName());
        student.setLastName(studentDTO.getLastName());
        student.setEmail(studentDTO.getEmail());
        student.setPhone(studentDTO.getPhone());
        student.setDateOfBirth(studentDTO.getDateOfBirth());
        student.setGender(studentDTO.getGender());
        student.setAddress(studentDTO.getAddress());
        student.setStatus(studentDTO.getStatus());

        student = studentRepository.save(student);
        return convertToDTO(student);
    }

    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

    // Filter Method
    public List<StudentDTO> filterStudents(String search, String status, String dateRange) {
        List<Student> students = studentRepository.findAll();

        return students.stream()
                .filter(student -> {
                    // Search filter
                    boolean matchesSearch = true;
                    if (search != null && !search.isEmpty()) {
                        String searchLower = search.toLowerCase();
                        matchesSearch = student.getStudentId().toLowerCase().contains(searchLower) ||
                                student.getFirstName().toLowerCase().contains(searchLower) ||
                                student.getLastName().toLowerCase().contains(searchLower) ||
                                student.getEmail().toLowerCase().contains(searchLower);
                    }

                    // Status filter
                    boolean matchesStatus = true;
                    if (status != null && !status.isEmpty() && !status.equals("all")) {
                        matchesStatus = student.getStatus() != null && student.getStatus().equals(status);
                    }

                    // Date range filter
                    boolean matchesDate = true;
                    if (dateRange != null && !dateRange.isEmpty() && !dateRange.equals("all")) {
                        LocalDate today = LocalDate.now();
                        LocalDate enrollmentDate = student.getEnrollmentDate();
                        if (enrollmentDate != null) {
                            long daysBetween = java.time.temporal.ChronoUnit.DAYS.between(enrollmentDate, today);
                            switch (dateRange) {
                                case "today":
                                    matchesDate = daysBetween == 0;
                                    break;
                                case "week":
                                    matchesDate = daysBetween <= 7;
                                    break;
                                case "month":
                                    matchesDate = daysBetween <= 30;
                                    break;
                                case "year":
                                    matchesDate = daysBetween <= 365;
                                    break;
                            }
                        }
                    }

                    return matchesSearch && matchesStatus && matchesDate;
                })
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private StudentDTO convertToDTO(Student student) {
        StudentDTO dto = new StudentDTO();
        dto.setId(student.getId());
        dto.setStudentId(student.getStudentId());
        dto.setFirstName(student.getFirstName());
        dto.setLastName(student.getLastName());
        dto.setFullName(student.getFullName());
        dto.setEmail(student.getEmail());
        dto.setPhone(student.getPhone());
        dto.setDateOfBirth(student.getDateOfBirth());
        dto.setGender(student.getGender());
        dto.setAddress(student.getAddress());
        dto.setEnrollmentDate(student.getEnrollmentDate());
        dto.setStatus(student.getStatus());
        return dto;
    }

    private Student convertToEntity(StudentDTO dto) {
        Student student = new Student();
        student.setStudentId(dto.getStudentId());
        student.setFirstName(dto.getFirstName());
        student.setLastName(dto.getLastName());
        student.setEmail(dto.getEmail());
        student.setPhone(dto.getPhone());
        student.setDateOfBirth(dto.getDateOfBirth());
        student.setGender(dto.getGender());
        student.setAddress(dto.getAddress());
        student.setStatus(dto.getStatus());
        return student;
    }
}